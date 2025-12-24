package day14

class Day14 {

  class Part1 {
    fun solve(input: String): Int {
      val robots: List<Robot> = parseInput(input)
      return solve(robots, dimensions = Dimensions(x = 101, y = 103), seconds = 100)
    }

    internal fun solve(robots: List<Robot>, dimensions: Dimensions, seconds: Int): Int {
      val finalPositions: List<Position> = getAllFinalPositions(robots, dimensions, seconds)
      val quadrantCounts = countQuadrants(finalPositions, dimensions)
      return quadrantCounts.topLeft *
        quadrantCounts.topRight *
        quadrantCounts.bottomLeft *
        quadrantCounts.bottomRight
    }

    private fun countQuadrants(
      finalPositions: List<Position>,
      dimensions: Dimensions,
    ): QuadrantCounts {
      val leftXRange = 0..<(dimensions.x / 2)
      val rightXRange = ((dimensions.x / 2) + 1)..<dimensions.x
      val topYRange = 0..<(dimensions.y / 2)
      val bottomYRange = ((dimensions.y / 2) + 1)..<dimensions.y

      var topLeftCount = 0
      var topRightCount = 0
      var bottomLeftCount = 0
      var bottomRightCount = 0

      for (p in finalPositions) {
        when {
          p.x in leftXRange && p.y in topYRange -> topLeftCount++
          p.x in leftXRange && p.y in bottomYRange -> bottomLeftCount++
          p.x in rightXRange && p.y in topYRange -> topRightCount++
          p.x in rightXRange && p.y in bottomYRange -> bottomRightCount++
        }
      }

      return QuadrantCounts(
        topLeft = topLeftCount,
        topRight = topRightCount,
        bottomLeft = bottomLeftCount,
        bottomRight = bottomRightCount,
      )
    }

    data class QuadrantCounts(
      val topLeft: Int,
      val topRight: Int,
      val bottomLeft: Int,
      val bottomRight: Int,
    )

    internal fun getAllFinalPositions(
      robots: List<Robot>,
      dimensions: Dimensions,
      seconds: Int,
    ): List<Position> = robots.map { robot -> getFinalPosition(robot, dimensions, seconds) }

    private fun getFinalPosition(robot: Robot, dimensions: Dimensions, seconds: Int): Position {
      var position = robot.position
      val velocity = robot.velocity
      repeat(times = seconds) {
        val newX = (position.x + velocity.x).mod(dimensions.x)
        val newY = (position.y + velocity.y).mod(dimensions.y)
        position = Position(x = newX, y = newY)
      }
      return position
    }
  }

  class Part2 {
    fun solve(input: String): Int {
      TODO()
    }
  }
}

internal fun parseInput(input: String): List<Robot> {
  return input.lines().map { line ->
    val xPosition = line.substringAfter("p=").substringBefore(',').toInt()
    val yPosition = line.substringAfter(',').substringBefore(" v=").toInt()

    val xVelocity = line.substringAfter("v=").substringBefore(',').toInt()
    val yVelocity = line.substringAfterLast(',').toInt()

    Robot(Position(x = xPosition, y = yPosition), Velocity(x = xVelocity, y = yVelocity))
  }
}

data class Robot(val position: Position, val velocity: Velocity)

/**
 * Each robot's position is given as p=x,y where x represents the number of tiles the robot is from
 * the left wall and y represents the number of tiles from the top wall (when viewed from above).
 */
data class Position(val x: Int, val y: Int)

/**
 * Each robot's velocity is given as v=x,y where x and y are given in tiles per second. Positive x
 * means the robot is moving to the right, and positive y means the robot is moving down.
 */
data class Velocity(val x: Int, val y: Int)

data class Dimensions(val x: Int, val y: Int)
