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
