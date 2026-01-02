package day18

import common.GridCoordinate

class Day18 {
  class Part1 {
    fun solve(input: String, maxRowOrColumnIndex: Int, numBytes: Int): Int {
      val inputCoordinates: List<GridCoordinate> = parseInput(input)
      val coordinatesToProcess = inputCoordinates.take(numBytes)
      val grid = createGrid(maxRowOrColumnIndex, coordinatesToProcess)

      val shortestPath: FindPathResult.ShortestPath =
        findShortestPathBfs(
          grid,
          endCoordinate = GridCoordinate(row = maxRowOrColumnIndex, column = maxRowOrColumnIndex),
        )
          as? FindPathResult.ShortestPath ?: throw IllegalStateException("No path found")

      return shortestPath.path.size - 1
    }
  }

  class Part2 {
    fun solve(input: String, maxRowOrColumnIndex: Int): String {
      val inputCoordinates: List<GridCoordinate> = parseInput(input)

      for ((index, coordinate) in inputCoordinates.withIndex()) {
        val coordinatesToProcess = inputCoordinates.take(index + 1)
        val grid = createGrid(maxRowOrColumnIndex, coordinatesToProcess)

        val findPathResult: FindPathResult =
          findShortestPathBfs(
            grid,
            endCoordinate = GridCoordinate(row = maxRowOrColumnIndex, column = maxRowOrColumnIndex),
          )

        if (findPathResult is FindPathResult.Unreachable) {
          return "${coordinate.column},${coordinate.row}"
        }
      }

      throw IllegalStateException("Path found after all bytes processed")
    }
  }
}

fun createGrid(maxRowOrColumnIndex: Int, corrupted: List<GridCoordinate>): Array<Array<Char>> {
  val grid = Array(maxRowOrColumnIndex + 1) { Array(maxRowOrColumnIndex + 1) { EMPTY } }

  for (coordinate in corrupted) {
    grid[coordinate.row][coordinate.column] = CORRUPTED
  }

  return grid
}

const val EMPTY = '.'
const val CORRUPTED = '#'
val START_COORDINATE = GridCoordinate(row = 0, column = 0)

// Each byte position is given as an X,Y coordinate, where X is the distance from the left edge of
// your memory space and Y is the distance from the top edge of your memory space.
internal fun parseInput(input: String): List<GridCoordinate> =
  input.lines().map { line ->
    val (x, y) = line.split(',').map { it.toInt() }
    GridCoordinate(row = y, column = x)
  }

sealed class FindPathResult {
  data class ShortestPath(val path: List<GridCoordinate>) : FindPathResult()

  data object Unreachable : FindPathResult()
}

private fun findShortestPathBfs(
  grid: Array<Array<Char>>,
  endCoordinate: GridCoordinate,
): FindPathResult {
  val queue = ArrayDeque<TraversalState>()
  queue.add(TraversalState(START_COORDINATE, listOf(START_COORDINATE)))

  val visited = mutableSetOf<GridCoordinate>()

  var minPath: List<GridCoordinate>? = null

  while (queue.isNotEmpty()) {
    val (coordinate, path) = queue.removeFirst()

    if (coordinate in visited) {
      continue
    } else {
      visited += coordinate
    }

    if (coordinate == endCoordinate) {
      if (minPath == null) {
        minPath = path
      } else if (path.size < minPath.size) {
        minPath = path
      }
    }

    val neighbors =
      setOf(coordinate.up, coordinate.down, coordinate.left, coordinate.right).filter {
        it.isValid(grid) && grid[it.row][it.column] != CORRUPTED
      }
    for (neighbor in neighbors) {
      queue.addLast(TraversalState(neighbor, path + neighbor))
    }
  }

  return if (minPath?.last() == endCoordinate) {
    FindPathResult.ShortestPath(minPath)
  } else {
    FindPathResult.Unreachable
  }
}

data class TraversalState(val coordinate: GridCoordinate, val path: List<GridCoordinate>)
