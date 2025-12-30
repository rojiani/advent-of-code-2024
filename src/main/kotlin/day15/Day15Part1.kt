package day15

import com.google.common.base.Preconditions.checkArgument
import common.GridCoordinate
import kotlin.collections.indices
import kotlin.collections.joinToString

/**
 * Day 15: Warehouse Woes
 *
 * https://adventofcode.com/2024/day/15
 */
class Day15Part1 {

  fun solve(input: String): Long {
    val grid = gridAfterMoves(input)
    return findBoxGpsSum(grid)
  }

  private fun findBoxGpsSum(grid: Array<Array<Space>>): Long =
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

  internal fun gridAfterMoves(input: String, numMoves: Int? = null): Array<Array<Space>> {
    val (grid, moves) = parseInput(input)
    return gridAfterMoves(grid, moves, numMoves ?: moves.size)
  }

  private fun gridAfterMoves(
    initialGrid: Array<Array<Space>>,
    moves: List<Move>,
    numMoves: Int,
  ): Array<Array<Space>> {
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

  data class MoveResult(val newGrid: Array<Array<Space>>, val newRobotPosition: GridCoordinate)
  
  private fun executeMove(
    grid: Array<Array<Space>>,
    robotPosition: GridCoordinate,
    move: Move,
  ): MoveResult? {
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
    grid: Array<Array<Space>>,
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

  private fun Array<Array<Space>>.get(coordinate: GridCoordinate): Space {
    return this[coordinate.row][coordinate.column]
  }

  private fun Array<Array<Space>>.set(coordinate: GridCoordinate, space: Space) {
    checkArgument(coordinate.isValid(this)) { "Can't set out of bounds space: $coordinate" }
    this[coordinate.row][coordinate.column] = space
  }

  internal fun parseInput(input: String): PuzzleInput {
    val mapLines = input.lines().takeWhile { it.startsWith('#') }
    val map: Array<Array<Space>> =
      mapLines.map { line -> line.map { Space.fromSymbol(it) }.toTypedArray() }.toTypedArray()

    val moveLines = input.lines().drop(mapLines.size + 1)
    val moves: List<Move> = moveLines.joinToString("").map { symbol -> Move.fromSymbol(symbol) }

    return PuzzleInput(map, moves)
  }

  data class PuzzleInput(val grid: Array<Array<Space>>, val moves: List<Move>)

  enum class Space(val symbol: Char) {
    WALL('#'),
    BOX('O'),
    ROBOT('@'),
    EMPTY('.');

    companion object {
      fun fromSymbol(symbol: Char): Space = Space.entries.first { it.symbol == symbol }
    }
  }

  internal fun state(grid: Array<Array<Space>>): String {
    return buildString {
        for (row in grid.indices) {
          val spaces = grid[row].joinToString("") { "${it.symbol}" }
          appendLine(spaces)
        }
      }
      .trim()
  }

  private fun getRobotStartingPosition(grid: Array<Array<Space>>): GridCoordinate {
    for (row in grid.indices) {
      for (col in grid[row].indices) {
        if (grid[row][col] == Space.ROBOT) {
          return GridCoordinate(row, col)
        }
      }
    }
    throw IllegalArgumentException("Robot not found in grid")
  }
}
