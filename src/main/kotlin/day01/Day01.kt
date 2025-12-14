package day01

import kotlin.math.abs

class Day01 {

  class Part1 {
    fun solve(input: String): Int {
      val (left, right) = parseInput(input)
      val sortedLeft = left.sorted()
      val sortedRight = right.sorted()

      var totalDistance = 0
      sortedLeft.zip(sortedRight).forEach { (left, right) -> totalDistance += abs(left - right) }

      return totalDistance
    }
  }

  // This time, you'll need to figure out exactly how often each number from the
  // left list
  // appears in the right list. Calculate a total similarity score by adding up
  // each number
  // in the left list after multiplying it by the number of times that number
  // appears in the
  // right list.
  class Part2 {
    fun solve(input: String): Int {
      val (left, right) = parseInput(input)

      val rightNumberCounts = right.groupingBy { it }.eachCount()
      return left.fold(0) { similarityScore, leftNumber ->
        similarityScore + (leftNumber * rightNumberCounts.getOrDefault(leftNumber, 0))
      }
    }
  }
}

internal fun parseInput(input: String): Pair<List<Int>, List<Int>> {
  val lines = input.lines().map { line -> line.trim() }
  val left = mutableListOf<Int>()
  val right = mutableListOf<Int>()
  for (line in lines) {
    val numbers = line.trim().split("\\s".toRegex()).filter { it.isNotEmpty() }.map { it.toInt() }
    left += numbers[0]
    right += numbers[1]
  }
  return Pair(left, right)
}
