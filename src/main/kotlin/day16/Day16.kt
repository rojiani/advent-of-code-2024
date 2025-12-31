package day16

import common.GridCoordinate
import kotlin.collections.map
import kotlin.collections.toList

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
  }

  class Part2 {
    fun solve(input: String): Int {
      val maze = parseInput(input)
      val startingPositionAndDirection = PositionAndDirection(findStart(maze), STARTING_DIRECTION)
      val minScorePaths: List<List<Step>> =
        findMinScorePaths(maze = maze, startingPositionAndDirection = startingPositionAndDirection)

      val bestPathTiles = tilesOnAnyBestPath(minScorePaths)
      printBestTiles(maze, bestPathTiles)
      return bestPathTiles.size
    }

    private fun printBestTiles(maze: List<List<Tile>>, bestPathTiles: Set<GridCoordinate>) {
      val result: MutableList<MutableList<Char>> =
        maze.map { row -> row.map { tile -> tile.symbol }.toMutableList() }.toMutableList()

      for (r in maze.indices) {
        for (c in maze[r].indices) {
          if (GridCoordinate(r, c) in bestPathTiles) {
            result[r][c] = 'O'
          }
        }
      }

      println(result.joinToString("\n") { row -> row.joinToString("") })
    }

    private fun tilesOnAnyBestPath(minScorePaths: List<List<Step>>): Set<GridCoordinate> =
      minScorePaths.flatMap { path -> path.map { it.position } }.toSet()

    private fun findMinScorePaths(
      maze: Maze,
      startingPositionAndDirection: PositionAndDirection,
    ): List<List<Step>> {
      val minScorePathFrom = mutableMapOf<PositionAndDirection, Int>()
      val minPathFrom = mutableMapOf<PositionAndDirection, List<Step>>()
      val minScorePaths = mutableListOf<List<Step>>()
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
          val path = steps + Step.End(position)
          if (score == minScoreToEnd) {
            minPathFrom[positionAndDirection] = path
            minScorePaths += path
          } else if (score < minScoreToEnd) {
            minPathFrom[positionAndDirection] = path
            minScorePaths.clear()
            minScorePaths += path
          }
          minScoreToEnd = minOf(minScoreToEnd, score)
        }

        if (
          positionAndDirection in minScorePathFrom &&
            minScorePathFrom.getValue(positionAndDirection) < score
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

      return minScorePaths.toList()
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

sealed class Step {
  abstract val cost: Int
  abstract val position: GridCoordinate

  data class ClockwiseOnce(override val position: GridCoordinate) : Step() {
    override val cost: Int = TURN_COST
  }

  data class ClockwiseTwice(override val position: GridCoordinate) : Step() {
    override val cost: Int = 2 * TURN_COST
  }

  data class CounterClockwiseOnce(override val position: GridCoordinate) : Step() {
    override val cost: Int = TURN_COST
  }

  data class Forward(override val position: GridCoordinate, val direction: Direction) : Step() {
    override val cost: Int = 1
  }

  data class End(override val position: GridCoordinate) : Step() {
    override val cost: Int = 0
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

fun Maze.tileAt(coordinate: GridCoordinate): Tile = this[coordinate.row][coordinate.column]

data class TraversalState(val positionAndDirection: PositionAndDirection, val steps: List<Step>) {
  val score: Int
    get() = steps.sumOf { it.cost }
}
