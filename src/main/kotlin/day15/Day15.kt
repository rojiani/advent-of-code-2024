package day15

import com.google.common.base.Preconditions.checkArgument
import common.GridCoordinate
import kotlin.collections.indices
import kotlin.collections.joinToString

typealias Grid = Array<Array<Space>>

/**
 * Day 15: Warehouse Woes
 *
 * https://adventofcode.com/2024/day/15
 */
class Day15 {
  class Part1 {
    fun solve(input: String): Long {
      val grid = gridAfterMoves(input)
      return findBoxGpsSum(grid)
    }

    private fun findBoxGpsSum(grid: Grid): Long =
      grid.indices.sumOf { row ->
        grid[row]
          .indices
          .filter { col -> grid[row][col] == Space.BOX }
          .sumOf { col -> gps(GridCoordinate(row, col)) }
      }

    // The GPS coordinate of a box is equal to 100 times its distance from the top edge of the map
    // plus its distance from the left edge of the map. (This process does not stop at wall tiles;
    // measure all the way to the edges of the map.)
    private fun gps(boxCoordinate: GridCoordinate): Long =
      (100L * boxCoordinate.row) + boxCoordinate.column.toLong()

    internal fun gridAfterMoves(input: String, numMoves: Int? = null): Grid {
      val (grid, moves) = parseInput(input)
      return gridAfterMoves(grid, moves, numMoves ?: moves.size)
    }

    private fun gridAfterMoves(initialGrid: Grid, moves: List<Move>, numMoves: Int): Grid {
      val robotStartingPosition = getRobotStartingPosition(initialGrid)
      var grid = initialGrid
      var robotPosition = robotStartingPosition
      for (moveIndex in 0 until numMoves) {
        val move = moves[moveIndex]
        val moveResult: MoveResult? = executeMove(grid, robotPosition, move)
        if (moveResult != null) {
          grid = moveResult.newGrid
          robotPosition = moveResult.newRobotPosition
        }
      }

      return grid
    }

    data class MoveResult(val newGrid: Grid, val newRobotPosition: GridCoordinate)

    private fun executeMove(grid: Grid, robotPosition: GridCoordinate, move: Move): MoveResult? {
      val newGrid = grid.copyOf()
      val nextCoordinate =
        when (move) {
          Move.UP -> robotPosition.up
          Move.DOWN -> robotPosition.down
          Move.LEFT -> robotPosition.left
          Move.RIGHT -> robotPosition.right
        }

      val newRobotPosition: GridCoordinate? = tryMove(newGrid, robotPosition, nextCoordinate, move)
      return if (newRobotPosition == null) {
        null
      } else {
        newGrid.set(robotPosition, Space.EMPTY)
        MoveResult(newGrid, newRobotPosition)
      }
    }

    private fun tryMove(
      grid: Grid,
      previous: GridCoordinate,
      current: GridCoordinate,
      move: Move,
    ): GridCoordinate? =
      when (grid.get(current)) {
        Space.WALL -> null
        Space.ROBOT -> throw IllegalArgumentException("Found a robot at $current")
        Space.EMPTY -> {
          grid.set(current, grid.get(previous))
          current
        }
        Space.BOX -> {
          val nextCoordinate =
            when (move) {
              Move.UP -> current.up
              Move.DOWN -> current.down
              Move.LEFT -> current.left
              Move.RIGHT -> current.right
            }
          if (tryMove(grid, previous = current, current = nextCoordinate, move = move) == null) {
            null
          } else {
            grid.set(current, grid.get(previous))
            current
          }
        }
      }

    private fun Grid.get(coordinate: GridCoordinate): Space {
      return this[coordinate.row][coordinate.column]
    }

    private fun Grid.set(coordinate: GridCoordinate, space: Space) {
      checkArgument(coordinate.isValid(this)) { "Can't set out of bounds space: $coordinate" }
      this[coordinate.row][coordinate.column] = space
    }
  }

  class Part2 {
    fun solve(input: String): Long {
      TODO()
    }
  }
}

internal fun parseInput(input: String): PuzzleInput {
  val mapLines = input.lines().takeWhile { it.startsWith('#') }
  val map: Array<Array<Space>> =
    mapLines.map { line -> line.map { Space.fromSymbol(it) }.toTypedArray() }.toTypedArray()

  val moveLines = input.lines().drop(mapLines.size + 1)
  val moves: List<Move> = moveLines.joinToString("").map { symbol -> Move.fromSymbol(symbol) }

  return PuzzleInput(map, moves)
}

data class PuzzleInput(val grid: Grid, val moves: List<Move>)

enum class Space(val symbol: Char) {
  WALL('#'),
  BOX('O'),
  ROBOT('@'),
  EMPTY('.');

  companion object {
    fun fromSymbol(symbol: Char): Space = Space.entries.first { it.symbol == symbol }
  }
}

enum class Move(val symbol: Char) {
  UP('^'),
  DOWN('v'),
  LEFT('<'),
  RIGHT('>');

  companion object {
    fun fromSymbol(symbol: Char): Move = Move.entries.first { it.symbol == symbol }
  }
}

internal fun Grid.state(): String {
  val grid = this
  return buildString {
      for (row in grid.indices) {
        val spaces = grid[row].joinToString("") { "${it.symbol}" }
        appendLine(spaces)
      }
    }
    .trim()
}

private fun getRobotStartingPosition(grid: Grid): GridCoordinate {
  for (row in grid.indices) {
    for (col in grid[row].indices) {
      if (grid[row][col] == Space.ROBOT) {
        return GridCoordinate(row, col)
      }
    }
  }
  throw IllegalArgumentException("Robot not found in grid")
}
