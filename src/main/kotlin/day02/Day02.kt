package day02

import kotlin.math.abs

typealias Report = List<Int>

/**
 * Day 2: Red-Nosed Reports
 *
 * https://adventofcode.com/2024/day/2
 */
class Day02 {

  class Part1 {
    fun solve(input: String): Int {
      val reports: List<Report> = parseInput(input)
      return reports.count { isSafe(it) }
    }
  }

  class Part2 {
    fun solve(input: String): Int {
      val reports: List<Report> = parseInput(input)
      return reports.count { isSafeWithRemoval(it) }
    }

    private fun isSafeWithRemoval(report: Report): Boolean {
      for (i in 0..report.lastIndex) {
        val reportWithRemoval = report.take(i) + report.drop(i + 1)
        if (isSafe(reportWithRemoval)) {
          return true
        }
      }
      return false
    }
  }
}

private fun isSafe(report: Report): Boolean {
  check(report.size > 1)

  val deltas = report.zipWithNext { a, b -> b - a }
  val allDecreasing = deltas.all { it < 0 && abs(it) in 1..3 }
  if (allDecreasing) {
    return true
  }
  return deltas.all { it > 0 && abs(it) in 1..3 }
}

internal fun parseInput(input: String): List<Report> =
  input.lines().map { it.trim() }.map { line -> line.split(" ").map { it.trim().toInt() } }
