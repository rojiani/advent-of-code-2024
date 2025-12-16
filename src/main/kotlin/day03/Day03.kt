package day03

import day03.Day03.Part1.MultiplyInstruction

/**
 * Day 3: Mull It Over
 *
 * https://adventofcode.com/2024/day/3
 */
class Day03 {

  class Part1 {
    fun solve(input: String): Int {
      // mul(X,Y), where X and Y are each 1-3 digit numbers
      val lines = input.lines().map { it.trim() }.filter { it.isNotEmpty() }
      val multiplyInstructions = lines.flatMap { line -> getMultiplicationInstructions(line) }

      return multiplyInstructions.sumOf { it.x * it.y }
    }

    data class MultiplyInstruction(val x: Int, val y: Int)
  }

  class Part2 {
    fun solve(input: String): Int {
      val lines = input.lines().map { it.trim() }.filter { it.isNotEmpty() }
      val joined = lines.joinToString("")

      val disabledRegionRanges: List<IntRange> =
        DISABLED_REGION_REGEX.findAll(joined).map { it.range }.toList()

      val enabledRegions = buildString {
        // It always starts enabled until reaching a "don't"
        append(joined.take(disabledRegionRanges.first().first))
        for (i in 0..<disabledRegionRanges.lastIndex) {
          append(
            joined.substring(
              disabledRegionRanges[i].last + 1 until disabledRegionRanges[i + 1].first
            )
          )
        }

        // add remaining
        append(joined.substring(disabledRegionRanges.last().last + 1, joined.length))
      }
      val multiplyInstructions = getMultiplicationInstructions(enabledRegions)
      return multiplyInstructions.sumOf { it.x * it.y }
    }
  }
}

internal fun getMultiplicationInstructions(line: String): List<MultiplyInstruction> {
  val matches = MULTIPLY_REGEX.findAll(line)
  return matches
    .map { matchResult ->
      val (x, y) = matchResult.destructured
      MultiplyInstruction(x.toInt(), y.toInt())
    }
    .toList()
}

private val MULTIPLY_REGEX = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
private val DO_REGEX = """do\(\)""".toRegex()
private val DONT_REGEX = """don't\(\)""".toRegex()
private val DISABLED_REGION_REGEX =
  """$DONT_REGEX(.*?)$DO_REGEX""".toRegex(RegexOption.DOT_MATCHES_ALL)
