package day17

import kotlin.collections.lastIndex
import kotlin.math.pow

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
    val numerator: Long = registerA.currentValue
    val denominator: Long = 2.0.pow(comboOperandValue).toLong()
    val quotient = numerator / denominator
    register.setValue(quotient)
    instructionPointer += 1
  }

  private fun executeBxl(instruction: Instruction, operand: Int) {
    registerB.xor(valueOf(operand, instruction.operandType).toLong())
    instructionPointer += 1
  }

  private fun executeBst(instruction: Instruction, operand: Int) {
    val operandValueMod8: Long = valueOf(operand, instruction.operandType).mod(8).toLong()
    registerB.setValue(operandValueMod8)
    instructionPointer += 1
  }

  private fun executeJnz(instruction: Instruction, operand: Int) {
    if (registerA.currentValue == 0L) {
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
    val operandValueMod8: Int = valueOf(operand, instruction.operandType).mod(8)
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
          4 -> registerA.currentValue.toInt()
          5 -> registerB.currentValue.toInt()
          6 -> registerC.currentValue.toInt()
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
  val registerAValue: Long,
  val registerBValue: Long,
  val registerCValue: Long,
  val output: String,
)
