package common

/** A `[row][column]` pair in a matrix. */
data class GridCoordinate(val row: Int, val column: Int) {
  fun <V> isValid(grid: List<List<V>>): Boolean = row in grid.indices && column in grid[row].indices
}
