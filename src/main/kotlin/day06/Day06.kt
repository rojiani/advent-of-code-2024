package day06

import common.GridCoordinate
import day06.Direction.DOWN
import day06.Direction.LEFT
import day06.Direction.RIGHT
import day06.Direction.UP

/**
 * Day 6: Guard Gallivant
 *
 * https://adventofcode.com/2024/day/6
 */
class Day06 {

  class Part1 {
    fun solve(input: String): Int {
      val grid: List<List<Char>> = parseInput(input)

      val startingPosition = findStartingPosition(grid)
      val guard = Guard(grid, startingPosition)

      val visited = guard.moveUntilExitingTheGrid()

      return visited.size
    }

    private fun findStartingPosition(grid: List<List<Char>>): GridCoordinate {
      for (row in grid.indices) {
        for (col in grid[row].indices) {
          if (grid[row][col] == GUARD) {
            return GridCoordinate(row, col)
          }
        }
      }
      throw IllegalArgumentException("Guard not found in grid")
    }
  }

  class Part2 {
    fun solve(input: String): Int {
      TODO()
    }
  }
}

internal fun parseInput(text: String): List<List<Char>> {
  return text.lines().map { it.trim().toList() }
}

private fun isValid(grid: List<List<Char>>, row: Int, col: Int): Boolean =
  row in grid.indices && col in grid[row].indices

const val GUARD = '^'
const val OBSTRUCTION = '#'

class Guard(private val grid: List<List<Char>>, start: GridCoordinate) {
  private var position = start
  private var direction: Direction = UP
  private val visited = mutableSetOf<GridCoordinate>()

  fun moveUntilExitingTheGrid(): Set<GridCoordinate> {
    while (true) {
      visited += position
      val next = nextPosition()
      when {
        !next.isValid(grid) -> return visited
        grid[next.row][next.column] == OBSTRUCTION -> {
          turnRight()
          continue
        }
        else -> {
          position = next
        }
      }
    }

    return visited
  }

  /**
   * Returns the next position, which may not be within the bounds of the grid, or may contain an
   * obstacle.
   */
  private fun nextPosition(): GridCoordinate =
    when (direction) {
      UP -> GridCoordinate(position.row - 1, position.column)
      RIGHT -> GridCoordinate(position.row, position.column + 1)
      DOWN -> GridCoordinate(position.row + 1, position.column)
      LEFT -> GridCoordinate(position.row, position.column - 1)
    }

  fun turnRight() {
    direction =
      when (direction) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
      }
  }
}

enum class Direction {
  UP,
  DOWN,
  LEFT,
  RIGHT,
}
