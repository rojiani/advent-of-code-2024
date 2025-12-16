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
       * Returns the next position, which may not be within the bounds of the grid, or may contain
       * an obstacle.
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
  }

  class Part2 {
    fun solve(input: String): Int {
      val grid: List<List<Char>> = parseInput(input)

      val startingPosition = findStartingPosition(grid)
      // find all possible places to put the obstruction
      val emptySpots = mutableSetOf<GridCoordinate>()
      for (r in grid.indices) {
        for (c in grid[r].indices) {
          if (grid[r][c] == EMPTY) {
            emptySpots += GridCoordinate(r, c)
          }
        }
      }

      val spotsToPutObstruction = mutableSetOf<GridCoordinate>()
      for (emptySpot in emptySpots) {
        if (formsACycle(grid, startingPosition, addedObstruction = emptySpot)) {
          spotsToPutObstruction += emptySpot
        }
      }
      return spotsToPutObstruction.size
    }

    private fun formsACycle(
      grid: List<List<Char>>,
      start: GridCoordinate,
      addedObstruction: GridCoordinate,
    ): Boolean {
      val guard = Guard(grid, start, addedObstruction)
      return guard.movesInCycle()
    }

    class Guard(
      private val grid: List<List<Char>>,
      start: GridCoordinate,
      private val addedObstruction: GridCoordinate,
    ) {
      private var position = start
      private var direction: Direction = UP

      // Keep track of both the positions visited and the direction to determine whether it
      // is truly a cycle. Positions alone isn't enough, as the guard may return to the starting
      // position but continue in a direction other than UP.
      private val visited = mutableSetOf<Pair<GridCoordinate, Direction>>()

      fun movesInCycle(): Boolean {
        while (true) {
          visited += Pair(position, direction)
          val next = nextPosition()
          if (Pair(next, direction) in visited) {
            return true
          }
          when {
            !next.isValid(grid) -> return false
            grid[next.row][next.column] == OBSTRUCTION ||
              GridCoordinate(next.row, next.column) == addedObstruction -> {
              turnRight()
              continue
            }
            else -> {
              position = next
            }
          }
        }

        return false
      }

      /**
       * Returns the next position, which may not be within the bounds of the grid, or may contain
       * an obstacle.
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
  }
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

internal fun parseInput(text: String): List<List<Char>> {
  return text.lines().map { it.trim().toList() }
}

const val GUARD = '^'
const val OBSTRUCTION = '#'
const val EMPTY = '.'

enum class Direction {
  UP,
  DOWN,
  LEFT,
  RIGHT,
}
