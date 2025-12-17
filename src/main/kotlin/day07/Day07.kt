package day07

/**
 * Day 7: Bridge Repair
 *
 * https://adventofcode.com/2024/day/7
 */
class Day07 {
  class Part1 {
    fun solve(input: String): Long {
      val equations = parseInput(input)

      return equations.filter { equation -> isSatisfiable(equation) }.sumOf { it.testValue }
    }

    private fun isSatisfiable(equation: Equation): Boolean =
      isSatisfiable(
        acc = equation.operands.first(),
        remainingOperands = equation.operands.drop(1),
        testValue = equation.testValue,
      )

    private fun isSatisfiable(acc: Long, remainingOperands: List<Long>, testValue: Long): Boolean {
      if (remainingOperands.isEmpty()) {
        return acc == testValue
      }

      return isSatisfiable(
        acc = acc + remainingOperands.first(),
        remainingOperands = remainingOperands.drop(1),
        testValue = testValue,
      ) ||
        isSatisfiable(
          acc = acc * remainingOperands.first(),
          remainingOperands = remainingOperands.drop(1),
          testValue = testValue,
        )
    }
  }

  class Part2 {
    fun solve(input: String): Long {
      val equations = parseInput(input)

      return equations.filter { equation -> isSatisfiable(equation) }.sumOf { it.testValue }
    }

    private fun isSatisfiable(equation: Equation): Boolean =
      isSatisfiable(
        acc = equation.operands.first(),
        remainingOperands = equation.operands.drop(1),
        testValue = equation.testValue,
      )

    internal fun isSatisfiable(acc: Long, remainingOperands: List<Long>, testValue: Long): Boolean {
      if (remainingOperands.isEmpty()) {
        return acc == testValue
      }

      // Add
      if (
        isSatisfiable(
          acc = acc + remainingOperands.first(),
          remainingOperands = remainingOperands.drop(1),
          testValue = testValue,
        )
      ) {
        return true
      }

      // Multiply
      if (
        isSatisfiable(
          acc = acc * remainingOperands.first(),
          remainingOperands = remainingOperands.drop(1),
          testValue = testValue,
        )
      ) {
        return true
      }

      // Concatenate
      return isSatisfiable(
        acc = "$acc${remainingOperands.first()}".toLong(),
        remainingOperands = remainingOperands.drop(1),
        testValue = testValue,
      )
    }
  }
}

private fun parseInput(input: String): List<Equation> {
  return input.lines().map { line ->
    val testValue = line.substringBefore(":").toLong()
    val operands =
      line.substringAfter(":").split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
    Equation(testValue, operands)
  }
}

data class Equation(val testValue: Long, val operands: List<Long>)
