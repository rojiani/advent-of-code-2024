package common

import com.google.common.base.Preconditions.checkArgument
import day15.Day15Part2.Space

/** A `[row][column]` pair in a matrix. */
data class GridCoordinate(val row: Int, val column: Int) {
  fun <V> isValid(grid: List<List<V>>): Boolean = row in grid.indices && column in grid[row].indices

  fun <V> isValid(grid: Array<Array<V>>): Boolean =
    row in grid.indices && column in grid[row].indices

  val up: GridCoordinate
    get() = GridCoordinate(row - 1, column)

  val down: GridCoordinate
    get() = GridCoordinate(row + 1, column)

  val left: GridCoordinate
    get() = GridCoordinate(row, column - 1)

  val right: GridCoordinate
    get() = GridCoordinate(row, column + 1)

  override fun toString() = "($row, $column)"
}

