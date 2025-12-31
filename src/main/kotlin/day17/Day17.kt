package day17

import kotlin.math.pow

/**
 * Day 17: Chronospatial Computer
 *
 * https://adventofcode.com/2024/day/17
 */
class Day17 {

  class Part1 {
    fun solve(input: String): String {
      val (registerA, registerB, registerC) = initializeRegisters(input)
      val programText: String =
        input.lines().single { it.startsWith("Program: ") }.substringAfter("Program: ")

      val computer = Computer(registerA, registerB, registerC, programText)
      val state = computer.run()
      return state.output
    }
  }

  class Part2 {
    fun solve(input: String): String {
      TODO()
    }
  }
}

class Computer(
  private val registerA: Register = Register(RegisterId.A, 0),
  private val registerB: Register = Register(RegisterId.B, 0),
  private val registerC: Register = Register(RegisterId.C, 0),
  programText: String,
) {

  init {
    require(registerA.id == RegisterId.A)
    require(registerB.id == RegisterId.B)
    require(registerC.id == RegisterId.C)
  }

  private var instructionPointer = 0
  private val output = mutableListOf<Int>()
  private val program: List<ProgramStatement> = compileProgram(programText)

  val state: ComputerState
    get() =
      ComputerState(
        registerA.currentValue,
        registerB.currentValue,
        registerC.currentValue,
        output.joinToString(","),
      )

  fun run(): ComputerState {
    while (instructionPointer <= program.lastIndex) {
      execute(program[instructionPointer])
    }

    return state
  }

  private fun execute(statement: ProgramStatement): ComputerState {
    val (instruction, operand) = statement

    when (instruction) {
      Instruction.BXC -> executeBxc()
      Instruction.ADV -> executeAdv(instruction, operand)
      Instruction.BXL -> executeBxl(instruction, operand)
      Instruction.BST -> executeBst(instruction, operand)
      Instruction.JNZ -> executeJnz(instruction, operand)
      Instruction.OUT -> executeOut(instruction, operand)
      Instruction.BDV -> executeBdv(instruction, operand)
      Instruction.CDV -> executeCdv(instruction, operand)
    }
    return state
  }

  private fun executeAdv(instruction: Instruction, operand: Int) {
    registerDivision(registerA, operand, instruction.operandType)
  }

  private fun executeBdv(instruction: Instruction, operand: Int) {
    registerDivision(registerB, operand, instruction.operandType)
  }

  private fun executeCdv(instruction: Instruction, operand: Int) {
    registerDivision(registerC, operand, instruction.operandType)
  }

  // adv, bdv, cdv
  private fun registerDivision(register: Register, operand: Int, operandType: OperandType?) {
    val comboOperandValue: Int = valueOf(operand, operandType)
    // The numerator is always the value in the A register.
    val numerator: Int = registerA.currentValue
    val denominator: Int = 2.0.pow(comboOperandValue).toInt()
    val quotient = numerator / denominator
    register.setValue(quotient)
    instructionPointer += 1
  }

  private fun executeBxl(instruction: Instruction, operand: Int) {
    registerB.xor(valueOf(operand, instruction.operandType))
    instructionPointer += 1
  }

  private fun executeBst(instruction: Instruction, operand: Int) {
    val operandValueMod8: Int = valueOf(operand, instruction.operandType) % 8
    registerB.setValue(operandValueMod8)
    instructionPointer += 1
  }

  private fun executeJnz(instruction: Instruction, operand: Int) {
    if (registerA.currentValue == 0) {
      instructionPointer += 1
      return
    }

    val operandValue: Int = valueOf(operand, instruction.operandType)

    check(operandValue % 2 == 0)

    // Divide by 2 because each ProgramStatement is 2 elements (Instruction & Operand)
    val newInstructionPointer = operandValue / 2
    if (newInstructionPointer == instructionPointer) {
      instructionPointer += 1
    } else {
      instructionPointer = newInstructionPointer
    }
  }

  private fun executeBxc() {
    registerB.xor(registerC.currentValue)
    instructionPointer += 1
  }

  private fun executeOut(instruction: Instruction, operand: Int) {
    val operandValueMod8: Int = valueOf(operand, instruction.operandType) % 8
    output += operandValueMod8
    instructionPointer += 1
  }

  private fun valueOf(operand: Int, operandType: OperandType?): Int {
    requireNotNull(operandType) { "Expected operand type for valueOf" }
    return when (operandType) {
      OperandType.LITERAL -> operand
      OperandType.COMBO ->
        when (operand) {
          in 0..3 -> operand
          4 -> registerA.currentValue
          5 -> registerB.currentValue
          6 -> registerC.currentValue
          else -> error("Unexpected combo operand: $operand")
        }
    }
  }

  private fun compileProgram(programText: String): List<ProgramStatement> {
    val numbers: List<Int> = programText.split(',').map { it.toInt() }

    return numbers.chunked(2).map {
      val instruction = Instruction.forOpcode(it[0])
      val operand = it[1]
      ProgramStatement(instruction, operand)
    }
  }
}

data class ComputerState(
  val registerAValue: Int,
  val registerBValue: Int,
  val registerCValue: Int,
  val output: String,
)

private fun initializeRegisters(input: String): Triple<Register, Register, Register> {
  val lines = input.lines()
  val registerA =
    Register(
      RegisterId.A,
      lines.single { it.startsWith("Register A: ") }.substringAfter("Register A: ").toInt(),
    )
  val registerB =
    Register(
      RegisterId.B,
      lines.single { it.startsWith("Register B: ") }.substringAfter("Register B: ").toInt(),
    )
  val registerC =
    Register(
      RegisterId.C,
      lines.single { it.startsWith("Register C: ") }.substringAfter("Register C: ").toInt(),
    )
  return Triple(registerA, registerB, registerC)
}

data class ProgramStatement(val instruction: Instruction, val operand: Int)

// This seems to be a 3-bit computer: its program is a list of 3-bit numbers (0 through 7), like
// 0,1,2,3.
// The computer also has three registers named A, B, and C, but these registers aren't
// limited to 3 bits and can instead hold any integer.
@JvmInline
value class Opcode(val id: Int) {
  init {
    check(id in 0..7)
  }
}

class Register(val id: RegisterId, initialValue: Int) {
  private var value = initialValue

  val currentValue: Int
    get() = value

  /** Perform bitwise XOR of the register's value and the given operand. */
  fun xor(operand: Int): Int {
    value = value xor operand
    return value
  }

  internal fun setValue(newValue: Int): Int {
    value = newValue
    return value
  }

  override fun toString(): String = "Register${id.name}($value)"
}

enum class RegisterId {
  A,
  B,
  C,
}

enum class Instruction(val opcode: Opcode, val operandType: OperandType? = null) {
  /**
   * The adv instruction (opcode 0) performs division. The numerator is the value in the A register.
   * The denominator is found by raising 2 to the power of the instruction's combo operand. (So, an
   * operand of 2 would divide A by 4 (2^2); an operand of 5 would divide A by 2^B.) The result of
   * the division operation is truncated to an integer and then written to the A register.
   */
  ADV(Opcode(0), OperandType.COMBO),

  /**
   * The bxl instruction (opcode 1) calculates the bitwise XOR of register B and the instruction's
   * literal operand, then stores the result in register B.
   */
  BXL(Opcode(1), OperandType.LITERAL),

  /**
   * The bst instruction (opcode 2) calculates the value of its combo operand modulo 8 (thereby
   * keeping only its lowest 3 bits), then writes that value to the B register.
   */
  BST(Opcode(2), OperandType.COMBO),

  /**
   * The jnz instruction (opcode 3) does nothing if the A register is 0. However, if the A register
   * is not zero, it jumps by setting the instruction pointer to the value of its literal operand;
   * if this instruction jumps, the instruction pointer is not increased by 2 after this
   * instruction.
   */
  JNZ(Opcode(3), OperandType.LITERAL),

  /**
   * The bxc instruction (opcode 4) calculates the bitwise XOR of register B and register C, then
   * stores the result in register B. (For legacy reasons, this instruction reads an operand but
   * ignores it.)
   */
  BXC(Opcode(4)),

  /**
   * The out instruction (opcode 5) calculates the value of its combo operand modulo 8, then outputs
   * that value. (If a program outputs multiple values, they are separated by commas.)
   */
  OUT(Opcode(5), OperandType.COMBO),

  /**
   * The bdv instruction (opcode 6) works exactly like the adv instruction except that the result is
   * stored in the B register. (The numerator is still read from the A register.)
   */
  BDV(Opcode(6), OperandType.COMBO),

  /**
   * The cdv instruction (opcode 7) works exactly like the adv instruction except that the result is
   * stored in the C register. (The numerator is still read from the A register.)
   */
  CDV(Opcode(7), OperandType.COMBO);

  companion object {
    fun forOpcode(value: Int): Instruction =
      Instruction.entries.single { it.opcode == Opcode(value) }
  }
}

/**
 * There are two types of operands; each instruction specifies the type of its operand. The value of
 * a literal operand is the operand itself. For example, the value of the literal operand 7 is the
 * number 7. The value of a combo operand can be found as follows:
 *
 * Combo operands 0 through 3 represent literal values 0 through 3. Combo operand 4 represents the
 * value of register A. Combo operand 5 represents the value of register B. Combo operand 6
 * represents the value of register C. Combo operand 7 is reserved and will not appear in valid
 * programs.
 */
enum class OperandType {
  LITERAL,
  COMBO,
}
