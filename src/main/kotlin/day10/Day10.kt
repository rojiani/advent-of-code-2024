package day10

import common.GridCoordinate

/**
 * Day 10: Hoof It
 *
 * https://adventofcode.com/2024/day/10
 */
class Day10 {

  class Part1 {
    fun solve(input: String): Int {
      val grid = parseInput(input)

      val trailheads: Set<GridCoordinate> = findTrailheads(grid)
      var trailheadScoreSum = 0
      for (trailhead in trailheads) {
        val paths = findHikingTrails(grid, trailhead)
        val trailheadScore = trailheadScore(paths)
        trailheadScoreSum += trailheadScore
      }
      return trailheadScoreSum
    }

    // a trailhead's score is the number of 9-height positions reachable from that trailhead via a
    // hiking trail
    private fun trailheadScore(pathsFromTrailhead: Set<List<GridCoordinate>>): Int =
      pathsFromTrailhead.map { it.last() }.distinct().size

    private fun findHikingTrails(
      grid: List<List<Char>>,
      trailhead: GridCoordinate,
    ): Set<List<GridCoordinate>> {
      val hikingTrails = mutableSetOf<List<GridCoordinate>>()
      findHikingTrails(
        grid = grid,
        current = trailhead,
        currentTrail = listOf(trailhead),
        targetHeight = TRAILHEAD,
        hikingTrails = hikingTrails,
        explored = mutableSetOf(),
      )
      return hikingTrails.toSet()
    }

    private fun findHikingTrails(
      grid: List<List<Char>>,
      current: GridCoordinate,
      currentTrail: List<GridCoordinate>,
      targetHeight: Char,
      hikingTrails: MutableSet<List<GridCoordinate>>,
      explored: MutableSet<List<GridCoordinate>>,
    ) {
      when {
        currentTrail in explored -> return

        !current.isValid(grid) || grid[current.row][current.column] != targetHeight -> {
          explored += currentTrail
          return
        }

        grid[current.row][current.column] == targetHeight && targetHeight == TRAIL_END -> {
          explored += currentTrail
          hikingTrails += currentTrail
          return
        }

        else -> {
          val nextMoves = setOf(current.up, current.down, current.left, current.right)
          for (next in nextMoves) {
            findHikingTrails(
              grid,
              current = next,
              currentTrail = currentTrail + next,
              targetHeight = targetHeight + 1,
              hikingTrails = hikingTrails,
              explored = explored,
            )
          }
        }
      }
    }
  }

  class Part2 {
    fun solve(input: String): Int {
      TODO()
    }
  }
}

const val TRAILHEAD = '0'
const val TRAIL_END = '9'

private fun parseInput(input: String): List<List<Char>> =
  input.lines().map { line -> line.toList() }

private fun findTrailheads(grid: List<List<Char>>): Set<GridCoordinate> = buildSet {
  for (r in grid.indices) {
    for (c in grid[r].indices) {
      if (grid[r][c] == TRAILHEAD) {
        add(GridCoordinate(r, c))
      }
    }
  }
}
