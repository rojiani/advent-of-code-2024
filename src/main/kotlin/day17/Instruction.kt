package day17

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

// This seems to be a 3-bit computer: its program is a list of 3-bit numbers (0 through 7), like
// 0,1,2,3.
// The computer also has three registers named A, B, and C, but these registers aren't
// limited to 3 bits and can instead hold any integer.
@JvmInline
value class Opcode(val id: Int) {
  init {
    require(id in 0..7)
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
