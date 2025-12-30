package day15

import com.google.common.base.Preconditions.checkArgument
import common.GridCoordinate
import kotlin.collections.indices
import kotlin.collections.joinToString
import kotlin.collections.map
import kotlin.collections.sortedBy
import kotlin.collections.sortedByDescending
import kotlin.collections.toTypedArray

/**
 * Day 15: Warehouse Woes
 *
 * https://adventofcode.com/2024/day/15
 */
class Day15Part2 {

  fun solve(input: String): Long {
    val grid = gridAfterMoves(input)
    return findBoxGpsSum(grid)
  }

  private fun findBoxGpsSum(grid: Array<Array<Space>>): Long =
    grid.indices.sumOf { row ->
      grid[row]
        .indices
        .filter { col -> grid[row][col] == Space.BOX_LEFT }
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
        // validateGrid(moveResult.newGrid)
        grid = moveResult.newGrid
        robotPosition = moveResult.newRobotPosition
      }
    }
    return grid
  }

  data class MoveResult(val newGrid: Array<Array<Space>>, val newRobotPosition: GridCoordinate)

  sealed class VerticalMoveResult {
    data object CantMove : VerticalMoveResult()

    data class Moved(val newRobotPosition: GridCoordinate) : VerticalMoveResult()
  }

  private fun executeMove(
    grid: Array<Array<Space>>,
    robotPosition: GridCoordinate,
    move: Move,
  ): MoveResult? {
    check(grid.get(robotPosition) == Space.ROBOT) { "Robot is not at $robotPosition" }
    val newGrid = grid.copyOf()

    val newRobotPosition: GridCoordinate? =
      if (move.isHorizontal) {
        val nextCoordinate =
          when (move) {
            Move.LEFT -> robotPosition.left
            Move.RIGHT -> robotPosition.right
            else -> throw IllegalArgumentException("Expected horizontal move: $move")
          }
        tryMoveHorizontal(newGrid, robotPosition, nextCoordinate, move)
      } else {
        when (
          val verticalMoveResult = tryMoveVertical(newGrid, robotPosition = robotPosition, move)
        ) {
          is VerticalMoveResult.Moved -> {
            newGrid.set(robotPosition, Space.EMPTY)
            newGrid.set(verticalMoveResult.newRobotPosition, Space.ROBOT)
            verticalMoveResult.newRobotPosition
          }
          VerticalMoveResult.CantMove -> null
        }
      }
    return if (newRobotPosition == null) {
      null
    } else {
      newGrid.set(robotPosition, Space.EMPTY)
      MoveResult(newGrid, newRobotPosition)
    }
  }

  private fun tryMoveHorizontal(
    grid: Array<Array<Space>>,
    previous: GridCoordinate,
    current: GridCoordinate,
    move: Move,
  ): GridCoordinate? {
    check(move.isHorizontal)
    return when (grid.get(current)) {
      Space.WALL -> null
      Space.ROBOT -> throw IllegalArgumentException("Found a robot at $current")
      Space.EMPTY -> {
        grid.set(current, grid.get(previous))
        current
      }

      Space.BOX_LEFT,
      Space.BOX_RIGHT -> {
        val nextCoordinate =
          if (move == Move.LEFT) {
            current.left
          } else {
            current.right
          }
        if (
          tryMoveHorizontal(grid, previous = current, current = nextCoordinate, move = move) == null
        ) {
          null
        } else {
          grid.set(current, grid.get(previous))
          current
        }
      }
    }
  }

  private fun findAffectedBoxes(
    grid: Array<Array<Space>>,
    start: GridCoordinate,
    move: Move,
  ): Set<Box> {
    val visited = mutableSetOf<GridCoordinate>()
    val affectedBoxes = mutableSetOf<Box>()

    val queue = ArrayDeque<GridCoordinate>()
    queue += start

    while (queue.isNotEmpty()) {
      val coordinate: GridCoordinate = queue.removeFirst()
      if (coordinate in visited) continue
      visited += coordinate

      if (!grid.get(coordinate).isBox()) continue

      if (!canMoveBoxVertically(grid, Box.fromSpace(grid.get(coordinate), coordinate), move)) {
        return emptySet()
      }

      affectedBoxes += Box.fromSpace(grid.get(coordinate), coordinate)

      val next =
        when (move) {
          Move.UP -> coordinate.up
          Move.DOWN -> coordinate.down
          else -> throw IllegalArgumentException("Expected vertical move: $move")
        }

      // add the other half of the box
      if (grid.get(coordinate) == Space.BOX_LEFT) {
        queue.addLast(coordinate.right)
      } else if (grid.get(coordinate) == Space.BOX_RIGHT) {
        queue.addLast(coordinate.left)
      }

      if (grid.get(next).isBox()) {
        val nextBox = Box.fromSpace(grid.get(next), next)
        queue.addLast(nextBox.left)
        queue.addLast(nextBox.right)
      }
    }

    return affectedBoxes
  }

  private fun canMoveBoxVertically(grid: Array<Array<Space>>, box: Box, move: Move): Boolean =
    when (move) {
      Move.UP -> grid.get(box.left.up) != Space.WALL && grid.get(box.right.up) != Space.WALL
      Move.DOWN -> grid.get(box.left.down) != Space.WALL && grid.get(box.right.down) != Space.WALL
      else -> throw IllegalArgumentException("Expected vertical move: $move")
    }

  private fun moveAffectedBoxes(grid: Array<Array<Space>>, affectedBoxes: Set<Box>, move: Move) {
    when (move) {
      Move.UP -> {
        // move box in top row up, then box(es) in next row
        val boxesByRow = affectedBoxes.sortedBy { it.row }
        for (box in boxesByRow) {
          val (left, right) = box
          grid.set(left.up, Space.BOX_LEFT)
          grid.set(left, Space.EMPTY)
          grid.set(right.up, Space.BOX_RIGHT)
          grid.set(right, Space.EMPTY)
        }
      }

      Move.DOWN -> {
        // move box in the lowest row down, then box(es) in next lowest row
        val boxesByDescendingRow = affectedBoxes.sortedByDescending { it.row }
        for (box in boxesByDescendingRow) {
          val (left, right) = box
          grid.set(left.down, Space.BOX_LEFT)
          grid.set(left, Space.EMPTY)
          grid.set(right.down, Space.BOX_RIGHT)
          grid.set(right, Space.EMPTY)
        }
      }

      else -> throw IllegalArgumentException("Expected vertical move: $move")
    }
  }

  private fun tryMoveVertical(
    grid: Array<Array<Space>>,
    robotPosition: GridCoordinate,
    move: Move,
  ): VerticalMoveResult {
    val startCoordinate =
      when (move) {
        Move.UP -> robotPosition.up
        Move.DOWN -> robotPosition.down
        else -> throw IllegalArgumentException("Expected vertical move: $move")
      }
    val startSpace = grid.get(startCoordinate)
    return when (startSpace) {
      Space.EMPTY -> VerticalMoveResult.Moved(startCoordinate)
      Space.WALL -> VerticalMoveResult.CantMove
      Space.ROBOT ->
        throw IllegalArgumentException("Found robot at both $robotPosition and $startCoordinate")
      Space.BOX_LEFT,
      Space.BOX_RIGHT -> {
        val affectedBoxes: Set<Box> = findAffectedBoxes(grid, startCoordinate, move)
        if (affectedBoxes.isEmpty()) {
          VerticalMoveResult.CantMove
        } else {
          moveAffectedBoxes(grid, affectedBoxes, move)
          VerticalMoveResult.Moved(startCoordinate)
        }
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
      mapLines
        .map { line ->
          line
            .flatMap { symbol ->
              when (symbol) {
                '#' -> listOf('#', '#')
                'O' -> listOf('[', ']')
                '.' -> listOf('.', '.')
                '@' -> listOf('@', '.')
                else -> throw IllegalArgumentException("Unknown symbol: $symbol")
              }
            }
            .map { char -> Space.fromSymbol(char) }
            .toTypedArray()
        }
        .toTypedArray()

    val moveLines = input.lines().drop(mapLines.size + 1)
    val moves: List<Move> = moveLines.joinToString("").map { symbol -> Move.fromSymbol(symbol) }

    return PuzzleInput(map, moves)
  }

  data class PuzzleInput(val grid: Array<Array<Space>>, val moves: List<Move>)

  enum class Space(val symbol: Char) {
    WALL('#'),
    BOX_LEFT('['),
    BOX_RIGHT(']'),
    ROBOT('@'),
    EMPTY('.');

    fun isBox(): Boolean = this == BOX_LEFT || this == BOX_RIGHT

    companion object {
      fun fromSymbol(symbol: Char): Space = Space.entries.first { it.symbol == symbol }
    }
  }

  data class Box(val left: GridCoordinate, val right: GridCoordinate) {
    init {
      check(left.row == right.row)
      check(left.column + 1 == right.column)
    }

    val row: Int = left.row

    companion object {
      fun fromSpace(space: Space, coordinate: GridCoordinate): Box {
        return when (space) {
          Space.BOX_LEFT -> Box(left = coordinate, right = coordinate.right)
          Space.BOX_RIGHT -> Box(left = coordinate.left, right = coordinate)
          else -> throw IllegalArgumentException("Expected box space, found $space")
        }
      }
    }
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

  // Output the grid (for debugging)
  internal fun state(grid: Array<Array<Space>>, includeIndices: Boolean = false): String {
    val indentSize = 4
    val output = buildString {
      val columnIndicesLabel = "    " + grid[0].indices.joinToString("") { (it % 10).toString() }
      if (includeIndices) {
        appendLine(columnIndicesLabel)
      }
      for (row in grid.indices) {
        if (includeIndices) {
          append(row.toString().padStart(indentSize - 1) + " ")
        }
        val spaces = grid[row].joinToString("") { "${it.symbol}" }
        appendLine(spaces)
      }
    }
    return if (!includeIndices) {
      output.trim()
    } else {
      output
    }
  }

  // Validate grid after a move
  private fun validateGrid(grid: Array<Array<Space>>) {
    val invalidPatterns = listOf("[[", "[.", "[#", "]]", "#]", ".]")
    for (row in grid.indices) {
      val rowString = grid[row].map { it.symbol }.joinToString("")
      for (pattern in invalidPatterns) {
        check(pattern !in rowString) { "Found invalid pattern: $pattern in grid: ${state(grid)}" }
      }
    }

    val robotPositions =
      grid.indices.flatMap { row -> grid[row].indices.filter { grid[row][it] == Space.ROBOT } }
    check(robotPositions.size == 1) {
      "Expected exactly one robot position, found: $robotPositions"
    }
  }
}
