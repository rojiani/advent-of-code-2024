package day04

/**
 * Day 4: Ceres Search
 *
 * https://adventofcode.com/2024/day/4
 */
class Day04 {

  class Part1 {
    fun solve(input: String): Int {
      val grid: List<List<Char>> = parseInput(input)

      var totalXmases = 0
      for (r in grid.indices) {
        for (c in grid[r].indices) {
          if (grid[r][c] == 'X') {
            totalXmases += countXmas(grid, startCoordinate = Coordinate(r, c))
          }
        }
      }
      return totalXmases
    }

    private fun countXmas(grid: List<List<Char>>, startCoordinate: Coordinate): Int {
      val (r, c) = startCoordinate
      val upLeft = (0..3).map { n -> Coordinate(r - n, c - n) }
      val up = (0..3).map { n -> Coordinate(r - n, c) }
      val upRight = (0..3).map { n -> Coordinate(r - n, c + n) }
      val left = (0..3).map { n -> Coordinate(r, c - n) }
      val right = (0..3).map { n -> Coordinate(r, c + n) }
      val leftDown = (0..3).map { n -> Coordinate(r + n, c - n) }
      val down = (0..3).map { n -> Coordinate(r + n, c) }
      val downRight = (0..3).map { n -> Coordinate(r + n, c + n) }

      val directions = setOf(upLeft, up, upRight, left, right, leftDown, down, downRight)
      val validDirections =
        directions.filter { direction -> direction.all { coordinate -> coordinate.isValid(grid) } }

      var xmasCount = 0
      for (coordinates in validDirections) {
        val word = coordinates.joinToString(separator = "") { (r, c) -> "${grid[r][c]}" }
        if (word == "XMAS") {
          xmasCount++
        }
      }
      return xmasCount
    }
  }

  class Part2 {
    fun solve(input: String): Int {
      val grid: List<List<Char>> = parseInput(input)

      var xmasCount = 0
      for (r in grid.indices) {
        for (c in grid[r].indices) {
          if (grid[r][c] == 'A') {
            if (isXmas(grid, startCoordinate = Coordinate(r, c))) {
              xmasCount++
            }
          }
        }
      }
      return xmasCount
    }

    private fun isXmas(grid: List<List<Char>>, startCoordinate: Coordinate): Boolean {
      val (r, c) = startCoordinate
      val topLeftToBottomRightCoordinates =
        setOf(Coordinate(r - 1, c - 1), Coordinate(r, c), Coordinate(r + 1, c + 1))
      val topRightToBottomLeftCoordinates =
        setOf(Coordinate(r - 1, c + 1), Coordinate(r, c), Coordinate(r + 1, c - 1))

      return isValidDiagonal(grid, topLeftToBottomRightCoordinates) &&
        isValidDiagonal(grid, topRightToBottomLeftCoordinates)
    }

    private fun isValidDiagonal(grid: List<List<Char>>, coordinates: Set<Coordinate>): Boolean =
      coordinates.all { it.isValid(grid) } &&
        coordinatesToString(grid, coordinates) in setOf("SAM", "MAS")

    private fun coordinatesToString(grid: List<List<Char>>, coordinates: Set<Coordinate>) =
      coordinates.joinToString(separator = "") { (r, c) -> "${grid[r][c]}" }
  }
}

private fun parseInput(text: String): List<List<Char>> = text.lines().map { it.toList() }

data class Coordinate(val row: Int, val column: Int) {
  fun isValid(grid: List<List<Char>>): Boolean = row in grid.indices && column in grid[row].indices
}
