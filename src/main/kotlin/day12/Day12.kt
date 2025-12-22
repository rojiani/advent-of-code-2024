package day12

import common.GridCoordinate
import kotlin.collections.plus

/**
 * Day 12: Garden Groups
 *
 * https://adventofcode.com/2024/day/12
 */
class Day12 {
  class Part1 {
    fun solve(input: String): Long {
      val grid: List<List<Plant>> = parseInput(input)
      val regionsByPlant: Map<Plant, List<Region>> = getAllRegions(grid)
      val regions: List<Region> = regionsByPlant.values.flatten()
      return regions.sumOf { it.fencePrice }
    }

    internal fun getAllRegions(grid: List<List<Plant>>): Map<Plant, List<Region>> {
      val plotsInARegion = mutableSetOf<Plot>()
      val regionsByPlant = mutableMapOf<Plant, List<Region>>()

      for (r in grid.indices) {
        for (c in grid[r].indices) {
          if (grid.plotAt(r, c) !in plotsInARegion) {
            val region = formRegion(grid, start = grid.plotAt(r, c), plotsInARegion)
            regionsByPlant[region.plant] =
              regionsByPlant.getOrDefault(region.plant, emptyList()) + region
          }
        }
      }

      return regionsByPlant
    }

    private fun formRegion(
      grid: List<List<Plant>>,
      start: Plot,
      plotsInARegion: MutableSet<Plot>,
    ): Region {
      val plotsInThisRegion = mutableSetOf<Plot>()

      val queue = ArrayDeque<GridCoordinate>()
      queue.addFirst(start.coordinate)
      val plant = grid[start.row][start.column]

      while (queue.isNotEmpty()) {
        val coordinate = queue.removeFirst()
        val plot = grid.plotAt(coordinate)
        plotsInARegion += plot
        plotsInThisRegion += plot

        val unvisitedNeighborsWithSamePlant =
          plot
            .neighbors(grid)
            .filter { grid[it.row][it.column] == plant }
            .filter { grid.plotAt(it) !in plotsInARegion }

        for (neighbor in unvisitedNeighborsWithSamePlant) {
          queue.addFirst(neighbor)
        }
      }

      return Region(plant = plant, plots = plotsInThisRegion.toSet(), grid = grid)
    }
  }

  class Part2 {
    fun solve(input: String): Long {
      TODO()
    }
  }
}

// Due to "modern" business practices, the price of fence required for a region is found by
// multiplying that region's area by its perimeter. The total price of fencing all regions on a map
// is found by adding together the price of fence for every region on the map.
// fun fencePrice()

@JvmInline value class Plant(private val id: Char)

/**
 * Each garden plot grows only a single type of plant and is indicated by a single letter on your
 * map.
 */
data class Plot(val plant: Plant, val row: Int, val column: Int) {
  constructor(
    plant: Plant,
    coordinate: GridCoordinate,
  ) : this(plant, row = coordinate.row, column = coordinate.column)

  val coordinate: GridCoordinate
    get() = GridCoordinate(row = row, column = column)

  /** Horizontal and vertical neighbors in the bounds of the grid */
  fun neighbors(grid: List<List<Plant>>): Set<GridCoordinate> =
    setOf(coordinate.up, coordinate.down, coordinate.left, coordinate.right)
      .filter { it.isValid(grid) }
      .toSet()
}

/**
 * When multiple garden plots are growing the same type of plant and are touching (horizontally or
 * vertically), they form a region.
 */
data class Region(val plant: Plant, val plots: Set<Plot>, val grid: List<List<Plant>>) {
  val plotCoordinates: Set<GridCoordinate>
    get() = plots.map { it.coordinate }.toSet()

  /** The area of a region is simply the number of garden plots the region contains. */
  val area: Int = plots.size

  /**
   * The perimeter of a region is the number of sides of garden plots in the region that do not
   * touch another garden plot in the same region.
   */
  val perimeter: Int
    get() {
      return plots.sumOf { plot ->
        val touchingAnotherPlot =
          plot.neighbors(grid).count { neighbor -> neighbor in plotCoordinates }
        4 - touchingAnotherPlot
      }
    }

  val fencePrice: Long = area.toLong() * perimeter.toLong()
}

internal fun parseInput(input: String): List<List<Plant>> =
  input.lines().map { line -> line.map { Plant(it) } }

private fun List<List<Plant>>.plotAt(row: Int, column: Int): Plot =
  Plot(plant = this[row][column], row = row, column = column)

private fun List<List<Plant>>.plotAt(coordinate: GridCoordinate): Plot =
  Plot(plant = this[coordinate.row][coordinate.column], coordinate = coordinate)
