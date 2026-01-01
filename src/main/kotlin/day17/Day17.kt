package day17

import kotlin.collections.joinToString
import kotlin.collections.lastIndex

/**
 * Day 17: Chronospatial Computer
 *
 * https://adventofcode.com/2024/day/17
 */
class Day17 {

  class Part1 {
    fun solve(input: String): String {
      val (registerA, registerB, registerC) = initializeRegisters(input)
      val programText: String = getProgramText(input)

      val computer = Computer(registerA, registerB, registerC, programText)
      val state = computer.run()
      return state.output
    }
  }

  class Part2 {
    fun solve(input: String): Long {
      val programText: String = getProgramText(input)
      val program: List<Int> = programText.split(",").map { it.toInt() }
      return backtrack(program = program) / 8
    }

    fun backtrack(a: Long = 0L, program: List<Int>, index: Int = program.lastIndex): Long {
      if (index < 0) {
        return a
      }
      val result: List<Long> = buildList {
        for (mod8 in 0..7) {
          val computer =
            Computer(Register(RegisterId.A, a + mod8), programText = program.joinToString(","))
          val output = computer.run().output
          if (output.first().digitToInt() == program[index]) {
            add(backtrack((a + mod8) * 8, program, index - 1))
          }
        }
      }
      return result.minOrNull() ?: Long.MAX_VALUE
    }
  }
}

private fun initializeRegisters(input: String): Triple<Register, Register, Register> {
  val lines = input.lines()
  val registerA =
    Register(
      RegisterId.A,
      lines.single { it.startsWith("Register A: ") }.substringAfter("Register A: ").toLong(),
    )
  val registerB =
    Register(
      RegisterId.B,
      lines.single { it.startsWith("Register B: ") }.substringAfter("Register B: ").toLong(),
    )
  val registerC =
    Register(
      RegisterId.C,
      lines.single { it.startsWith("Register C: ") }.substringAfter("Register C: ").toLong(),
    )
  return Triple(registerA, registerB, registerC)
}

private fun getProgramText(input: String): String =
  input.lines().single { it.startsWith("Program: ") }.substringAfter("Program: ")
