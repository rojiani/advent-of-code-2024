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
      val regionsByPlant: Map<Plant, List<Region>> =
        getAllRegions(grid, fencePricing = FencePricing.STANDARD)
      val regions: List<Region> = regionsByPlant.values.flatten()
      return regions.sumOf { it.fencePrice }
    }
  }

  class Part2 {
    fun solve(input: String): Long {
      val grid: List<List<Plant>> = parseInput(input)
      val regionsByPlant: Map<Plant, List<Region>> =
        getAllRegions(grid, fencePricing = FencePricing.BULK_DISCOUNT)
      val regions: List<Region> = regionsByPlant.values.flatten()
      return regions.sumOf { it.fencePrice }
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
data class Region(
  val plant: Plant,
  val plots: Set<Plot>,
  val grid: List<List<Plant>>,
  val fencePricing: FencePricing,
) {
  val plotCoordinates: Set<GridCoordinate>
    get() = plots.map { it.coordinate }.toSet()

  /** The area of a region is simply the number of garden plots the region contains. */
  val area: Int = plots.size

  /**
   * The perimeter of a region is the number of sides of garden plots in the region that do not
   * touch another garden plot in the same region.
   */
  val perimeter: Int
    get() =
      when (fencePricing) {
        FencePricing.STANDARD ->
          plots.sumOf { plot ->
            val touchingAnotherPlot =
              plot.neighbors(grid).count { neighbor -> neighbor in plotCoordinates }
            4 - touchingAnotherPlot
          }
        // Count the number of corners - this will equal the # of sides.
        FencePricing.BULK_DISCOUNT -> plots.sumOf { it.numCorners() }
      }

  val fencePrice: Long
    get() = area.toLong() * perimeter.toLong()

  private fun Plot.numCorners(): Int =
    listOf(
        hasInnerTopLeftCorner(),
        hasInnerBottomLeftCorner(),
        hasInnerTopRightCorner(),
        hasInnerBottomRightCorner(),
        hasOuterTopLeftCorner(),
        hasOuterBottomLeftCorner(),
        hasOuterTopRightCorner(),
        hasOuterBottomRightCorner(),
      )
      .count { it }

  private fun Plot.hasInnerTopLeftCorner(): Boolean =
    setOf(coordinate.up, coordinate.left).none { it in plotCoordinates }

  private fun Plot.hasInnerBottomLeftCorner(): Boolean =
    setOf(coordinate.left, coordinate.down).none { it in plotCoordinates }

  private fun Plot.hasInnerTopRightCorner(): Boolean =
    setOf(coordinate.up, coordinate.right).none { it in plotCoordinates }

  private fun Plot.hasInnerBottomRightCorner(): Boolean =
    setOf(coordinate.right, coordinate.down).none { it in plotCoordinates }

  private fun Plot.hasOuterTopLeftCorner(): Boolean =
    setOf(coordinate.up, coordinate.left).all { it in plotCoordinates } &&
      GridCoordinate(coordinate.row - 1, coordinate.column - 1) !in plotCoordinates

  private fun Plot.hasOuterTopRightCorner(): Boolean =
    setOf(coordinate.up, coordinate.right).all { it in plotCoordinates } &&
      GridCoordinate(coordinate.row - 1, coordinate.column + 1) !in plotCoordinates

  private fun Plot.hasOuterBottomLeftCorner(): Boolean =
    setOf(coordinate.down, coordinate.left).all { it in plotCoordinates } &&
      GridCoordinate(coordinate.row + 1, coordinate.column - 1) !in plotCoordinates

  private fun Plot.hasOuterBottomRightCorner(): Boolean =
    setOf(coordinate.down, coordinate.right).all { it in plotCoordinates } &&
      GridCoordinate(coordinate.row + 1, coordinate.column + 1) !in plotCoordinates
}

private fun formRegion(
  grid: List<List<Plant>>,
  start: Plot,
  plotsInARegion: MutableSet<Plot>,
  fencePricing: FencePricing,
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

  return Region(plant = plant, plots = plotsInThisRegion.toSet(), grid = grid, fencePricing)
}

internal fun getAllRegions(
  grid: List<List<Plant>>,
  fencePricing: FencePricing,
): Map<Plant, List<Region>> {
  val plotsInARegion = mutableSetOf<Plot>()
  val regionsByPlant = mutableMapOf<Plant, List<Region>>()

  for (r in grid.indices) {
    for (c in grid[r].indices) {
      if (grid.plotAt(r, c) !in plotsInARegion) {
        val region = formRegion(grid, start = grid.plotAt(r, c), plotsInARegion, fencePricing)
        regionsByPlant[region.plant] =
          regionsByPlant.getOrDefault(region.plant, emptyList()) + region
      }
    }
  }

  return regionsByPlant
}

internal fun parseInput(input: String): List<List<Plant>> =
  input.lines().map { line -> line.map { Plant(it) } }

private fun List<List<Plant>>.plotAt(row: Int, column: Int): Plot =
  Plot(plant = this[row][column], row = row, column = column)

private fun List<List<Plant>>.plotAt(coordinate: GridCoordinate): Plot =
  Plot(plant = this[coordinate.row][coordinate.column], coordinate = coordinate)

enum class FencePricing {
  /** Part 1 */
  STANDARD,
  /** Part 2 */
  BULK_DISCOUNT,
}
