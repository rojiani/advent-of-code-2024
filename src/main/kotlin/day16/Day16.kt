package day16

import common.GridCoordinate
import kotlin.collections.map

typealias Maze = List<List<Tile>>

/**
 * Day 16: Reindeer Maze
 *
 * https://adventofcode.com/2024/day/16
 */
class Day16 {

  class Part1 {
    fun solve(input: String): Int {
      val maze = parseInput(input)
      val startingPositionAndDirection = PositionAndDirection(findStart(maze), STARTING_DIRECTION)
      return findMinScorePath(
        maze = maze,
        startingPositionAndDirection = startingPositionAndDirection,
      )
    }

    private fun findMinScorePath(
      maze: Maze,
      startingPositionAndDirection: PositionAndDirection,
    ): Int {
      val minScorePathFrom = mutableMapOf<PositionAndDirection, Int>()
      val minPathFrom = mutableMapOf<PositionAndDirection, List<Step>>()

      var minScoreToEnd: Int = Int.MAX_VALUE

      val queue = ArrayDeque<TraversalState>()
      queue.addLast(TraversalState(startingPositionAndDirection, steps = emptyList()))

      while (queue.isNotEmpty()) {
        val traversalState = queue.removeFirst()
        val score = traversalState.score
        val positionAndDirection = traversalState.positionAndDirection
        val steps = traversalState.steps
        val position = traversalState.positionAndDirection.position

        if (!position.isValid(maze)) {
          continue
        }

        if (maze.tileAt(position) == Tile.WALL) {
          continue
        }

        if (maze.tileAt(position) == Tile.END) {
          if (score < minScoreToEnd) {
            minPathFrom[positionAndDirection] = steps.toList()
          }
          minScoreToEnd = minOf(minScoreToEnd, score)
        }

        if (
          positionAndDirection in minScorePathFrom &&
            minScorePathFrom.getValue(positionAndDirection) <= score
        ) {
          continue
        } else {
          minScorePathFrom[positionAndDirection] = score
          minPathFrom[positionAndDirection] = steps.toList()
        }

        // forward in the same direction
        queue.addLast(
          TraversalState(
            positionAndDirection.forwardPosition,
            steps = steps + Step.Forward(position, positionAndDirection.direction),
          )
        )

        // turn clockwise once
        queue.addLast(
          TraversalState(
            positionAndDirection.clockwiseOnce,
            steps = steps + Step.ClockwiseOnce(position),
          )
        )

        // turn clockwise twice
        queue.addLast(
          TraversalState(
            positionAndDirection.clockwiseTwice,
            steps = steps + Step.ClockwiseTwice(position),
          )
        )

        // counter-clockwise once
        queue.addLast(
          TraversalState(
            positionAndDirection.counterClockwiseOnce,
            steps = steps + Step.CounterClockwiseOnce(position),
          )
        )
      }

      return minScoreToEnd
    }

    fun Maze.tileAt(coordinate: GridCoordinate): Tile = this[coordinate.row][coordinate.column]

    data class TraversalState(
      val positionAndDirection: PositionAndDirection,
      val steps: List<Step>,
    ) {
      val score: Int
        get() = steps.sumOf { it.cost }
    }

    sealed class Step {
      abstract val debugString: String
      abstract val cost: Int
      abstract val position: GridCoordinate

      data class ClockwiseOnce(override val position: GridCoordinate) : Step() {
        override val debugString: String = "CW1x"
        override val cost: Int = TURN_COST
      }

      data class ClockwiseTwice(override val position: GridCoordinate) : Step() {
        override val debugString: String = "CW2x"
        override val cost: Int = 2 * TURN_COST
      }

      data class CounterClockwiseOnce(override val position: GridCoordinate) : Step() {
        override val debugString: String = "CCW1x"
        override val cost: Int = TURN_COST
      }

      data class Forward(override val position: GridCoordinate, val direction: Direction) : Step() {
        override val debugString: String = direction.symbol.toString()
        override val cost: Int = 1
      }
    }
  }

  class Part2 {
    fun solve(input: String): Int {
      TODO()
    }
  }
}

internal fun parseInput(input: String): Maze =
  input.lines().map { line -> line.map { char -> Tile.fromSymbol(char) } }

enum class Tile(val symbol: Char) {
  START('S'),
  END('E'),
  WALL('#'),
  EMPTY('.');

  companion object {
    fun fromSymbol(symbol: Char): Tile = Tile.values().first { it.symbol == symbol }
  }
}

data class PositionAndDirection(val position: GridCoordinate, val direction: Direction) {
  override fun toString(): String = "($position, ${direction.symbol})"

  val forwardPosition: PositionAndDirection
    get() {
      val newPosition =
        when (direction) {
          Direction.NORTH -> position.up
          Direction.SOUTH -> position.down
          Direction.WEST -> position.left
          Direction.EAST -> position.right
        }
      return PositionAndDirection(newPosition, direction)
    }

  val clockwiseOnce: PositionAndDirection
    get() = PositionAndDirection(position, direction.turnClockwise())

  val clockwiseTwice: PositionAndDirection
    get() = PositionAndDirection(position, direction.turnClockwise().turnClockwise())

  val counterClockwiseOnce: PositionAndDirection
    get() = PositionAndDirection(position, direction.turnCounterClockwise())
}

enum class Direction(val symbol: Char) {
  NORTH('^'),
  SOUTH('v'),
  WEST('<'),
  EAST('>');

  fun turnClockwise(): Direction =
    when (this) {
      NORTH -> EAST
      EAST -> SOUTH
      SOUTH -> WEST
      WEST -> NORTH
    }

  fun turnCounterClockwise(): Direction =
    when (this) {
      NORTH -> WEST
      WEST -> SOUTH
      SOUTH -> EAST
      EAST -> NORTH
    }
}

private fun findStart(maze: Maze): GridCoordinate {
  for (row in maze.indices) {
    for (col in maze[row].indices) {
      if (maze[row][col] == Tile.START) {
        return GridCoordinate(row, col)
      }
    }
  }
  throw IllegalArgumentException("Start not found in grid")
}

val STARTING_DIRECTION = Direction.EAST
const val TURN_COST: Int = 1000
