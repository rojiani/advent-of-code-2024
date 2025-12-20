package day08

import common.GridCoordinate
import day08.Day08.Part1.AntennaPair
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator
import kotlin.math.abs

/**
 * Day 8: Resonant Collinearity
 *
 * https://adventofcode.com/2024/day/8
 */
class Day08 {
  class Part1 {
    fun solve(input: String): Int {
      val grid = parseInput(input)

      val antennas: Map<Char, List<GridCoordinate>> = findAntennas(grid)
      val antennaPairs = pairAntennas(antennas)

      val antiNodes = mutableSetOf<GridCoordinate>()
      for (pair in antennaPairs) {
        val (_, p1, p2) = pair

        val pointRelationship = PointRelationship.of(p1, p2)
        val rowDiff = abs(p1.row - p2.row)
        val columnDiff = abs(p1.column - p2.column)

        val antiNodesForPair =
          when (pointRelationship) {
            PointRelationship.VERTICAL ->
              setOf(
                GridCoordinate(p1.row - rowDiff, p1.column),
                GridCoordinate(p2.row + rowDiff, p2.column),
              )
            PointRelationship.HORIZONTAL ->
              setOf(
                GridCoordinate(p1.row, p1.column - columnDiff),
                GridCoordinate(p2.row, p2.column + columnDiff),
              )
            PointRelationship.UP_AND_RIGHT ->
              setOf(
                GridCoordinate(p1.row + rowDiff, p1.column - columnDiff),
                GridCoordinate(p2.row - rowDiff, p2.column + columnDiff),
              )
            PointRelationship.DOWN_AND_RIGHT ->
              setOf(
                GridCoordinate(p1.row - rowDiff, p1.column - columnDiff),
                GridCoordinate(p2.row + rowDiff, p2.column + columnDiff),
              )
          }
        antiNodesForPair.filter { it.isValid(grid) }.forEach { antiNodes += it }
      }

      return antiNodes.size
    }

    data class AntennaPair(val signal: Char, val c1: GridCoordinate, val c2: GridCoordinate) {
      override fun toString(): String = "AntennaPair($signal, $c1, $c2)"
    }
  }

  class Part2 {
    fun solve(input: String): Int {
      val grid = parseInput(input)

      val antennas: Map<Char, List<GridCoordinate>> = findAntennas(grid)
      val antennaPairs = pairAntennas(antennas)

      val antiNodes = mutableSetOf<GridCoordinate>()
      for (pair in antennaPairs) {
        val (_, p1, p2) = pair

        val pointRelationship = PointRelationship.of(p1, p2)
        val rowDiff = abs(p1.row - p2.row)
        val columnDiff = abs(p1.column - p2.column)

        val antiNodesForPair =
          when (pointRelationship) {
            PointRelationship.VERTICAL ->
              buildSet {
                // Extend from upper point (p1)
                for (row in p1.row downTo 0 step rowDiff) {
                  add(GridCoordinate(row, p1.column))
                }
                // Extend from upper point (p1) down to the end of the grid
                for (row in p1.row..grid.lastIndex step rowDiff) {
                  add(GridCoordinate(row, p1.column))
                }
              }

            PointRelationship.HORIZONTAL ->
              buildSet {
                // Extend from left point (p1) to the left edge
                for (col in p1.column downTo 0 step columnDiff) {
                  add(GridCoordinate(p1.row, col))
                }
                // Extend from left point (p1) to the right edge
                for (col in p1.column..grid[p1.row].lastIndex step columnDiff) {
                  add(GridCoordinate(p1.row, col))
                }
              }

            PointRelationship.UP_AND_RIGHT ->
              buildSet {
                // Extend from the bottom-left point (p1) down and left
                var r = p1.row
                var c = p1.column
                while (GridCoordinate(r, c).isValid(grid)) {
                  add(GridCoordinate(r, c))
                  r += rowDiff
                  c -= columnDiff
                }
                // Extend from the bottom-left point (p1) up and right
                r = p1.row
                c = p1.column
                while (GridCoordinate(r, c).isValid(grid)) {
                  add(GridCoordinate(r, c))
                  r -= rowDiff
                  c += columnDiff
                }
              }
            PointRelationship.DOWN_AND_RIGHT ->
              buildSet {
                // Extend from the upper-left point (p1) up and left
                var r = p1.row
                var c = p1.column
                while (GridCoordinate(r, c).isValid(grid)) {
                  add(GridCoordinate(r, c))
                  r -= rowDiff
                  c -= columnDiff
                }
                // Extend from the upper-left point (p1) down and right
                r = p1.row
                c = p1.column
                while (GridCoordinate(r, c).isValid(grid)) {
                  add(GridCoordinate(r, c))
                  r += rowDiff
                  c += columnDiff
                }
              }
          }
        antiNodes += antiNodesForPair
      }

      return antiNodes.size
    }
  }
}

private fun parseInput(input: String): List<List<Char>> = input.lines().map { it.toList() }

private const val EMPTY = '.'

private fun sortPair(p1: GridCoordinate, p2: GridCoordinate): Pair<GridCoordinate, GridCoordinate> {
  val points = setOf(p1, p2)
  return when {
    // Vertical - p1 = top, p2 = bottom
    p1.column == p2.column -> {
      val top = points.minBy { it.row }
      val bottom = points.maxBy { it.row }
      top to bottom
    }
    // Horizontal - p1 = left, p2 = right
    p1.row == p2.row -> {
      val left = points.minBy { it.column }
      val right = points.maxBy { it.column }
      left to right
    }
    // \: p1 topLeft, p2 bottomR: p1.row < p2.row && p1.col < p2.col
    p1.row < p2.row && p1.column < p2.column -> p1 to p2
    // /: p1 bottomLeft, p2 upperR: p1.row > p2.row && p1.col < p2.col
    p1.row > p2.row && p1.column < p2.column -> p1 to p2
    // swap to make it \ or /
    else -> p2 to p1
  }
}

enum class PointRelationship {
  /** `\`: p1 topLeft, p2 bottomR: p1.row < p2.row && p1.col < p2.col */
  DOWN_AND_RIGHT,
  /** `|`: p1 higher: p1.row < p2.row && p1.col == p2.col */
  VERTICAL,
  /** `/`: p1 bottomLeft, p2 upperR: p1.row > p2.row && p1.col < p2.col */
  UP_AND_RIGHT,
  // `-`: p1 left: p1.row == p2.row && p1.col < p2.col
  HORIZONTAL;

  companion object {
    fun of(p1: GridCoordinate, p2: GridCoordinate): PointRelationship =
      when {
        p1.column == p2.column && p1.row < p2.row -> VERTICAL
        p1.row == p2.row && p1.column < p2.column -> HORIZONTAL
        p1.row < p2.row && p1.column < p2.column -> DOWN_AND_RIGHT
        p1.row > p2.row && p1.column < p2.column -> UP_AND_RIGHT
        else -> throw IllegalArgumentException("Invalid p1/p2 ordering: p1=$p1, p2=$p2")
      }
  }
}

private fun findAntennas(grid: List<List<Char>>): Map<Char, List<GridCoordinate>> {
  val antennas = mutableMapOf<Char, List<GridCoordinate>>()
  for (r in grid.indices) {
    for (c in grid[r].indices) {
      if (grid[r][c] != EMPTY) {
        antennas[grid[r][c]] =
          antennas.getOrDefault(grid[r][c], emptyList()) + listOf(GridCoordinate(r, c))
      }
    }
  }

  return antennas
}

/**
 * Create a list of (p1, p2) pairs of antennas. The (p1, p2) pairs will be ordered so that p1.row <=
 * p2.row, and for points forming horizontal lines, p1.column < p2.column.
 */
private fun pairAntennas(antennas: Map<Char, List<GridCoordinate>>): List<AntennaPair> {
  val antennaPairs = mutableListOf<AntennaPair>()
  for ((signal, coordinates) in antennas) {
    for ((i, c1) in coordinates.withIndex()) {
      for (j in i + 1..coordinates.lastIndex) {
        val (p1, p2) = sortPair(c1, coordinates[j])
        antennaPairs += AntennaPair(signal, p1, p2)
      }
    }
  }
  return antennaPairs
}
