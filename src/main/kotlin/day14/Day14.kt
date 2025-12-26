package day14

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.StreamingGifWriter
import common.GridCoordinate
import java.awt.Color
import java.awt.image.BufferedImage
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Duration
import javax.imageio.ImageIO
import kotlin.io.path.extension

/**
 * Day 14: Restroom Redoubt
 *
 * https://adventofcode.com/2024/day/14
 */
class Day14 {

  class Part1 {
    fun solve(input: String): Int {
      val robots: List<Robot> = parseInput(input)
      return solve(robots, dimensions = DEFAULT_DIMENSIONS, seconds = 100)
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
      val initialRobots = parseInput(input)
      val gridState = GridState(dimensions = DEFAULT_DIMENSIONS, initialRobots = initialRobots)
      val imagePaths = mutableListOf<Path>()
      var frameNumber = 0
      for (seconds in 1..10500) {
        gridState.moveAllRobotsOnce()
        // After inspecting the first few hundred images, the robots seem to cluster in
        // horizontal or vertical bands at 49, 152, 255, ..., and at 98, 199, 300,...
        if ((seconds - 98) % 101 == 0 || (seconds - 49) % 103 == 0) {
          frameNumber++
          val outputPath = Paths.get("grid-$seconds.png")
          gridState.saveGridImage(outputPath = outputPath, scale = 2)
          imagePaths.add(outputPath)
          // The frameNumber in the GIF
          println("frameNumber $frameNumber -> $outputPath")
        }
      }

      createGif(imagePaths.toList())

      throw IllegalArgumentException(
        "Inspect the frames in the generated GIF. Find the Xmas tree, which will look like ./grid-7774.png"
      )
    }

    private fun createGif(imagePaths: List<Path>): Path {
      val writer =
        StreamingGifWriter(
          /* frameDelay= */ Duration.ofMillis(100),
          /* infiniteLoop=*/ false,
          /* compressed= */ false,
        )
      val gifPath = Paths.get("robots.gif")
      val gifStream: StreamingGifWriter.GifStream =
        writer.prepareStream(gifPath, BufferedImage.TYPE_INT_ARGB)
      for (imagePath in imagePaths) {
        gifStream.writeFrame(ImmutableImage.loader().fromPath(imagePath))
      }
      gifStream.close()
      return gifPath
    }

    class GridState(val dimensions: Dimensions, private val initialRobots: List<Robot>) {

      private var robots = initialRobots
      private var positions = initialRobots.map { it.position }.toSet()
      private var seconds: Int = 0

      fun moveAllRobotsOnce() {
        seconds++
        robots = robots.map { robot -> robot.moveOnce(dimensions) }
        positions = robots.map { it.position }.toSet()
      }

      // Move a single robot once
      private fun Robot.moveOnce(dimensions: Dimensions): Robot =
        copy(
          position =
            Position(
              x = (position.x + velocity.x).mod(dimensions.x),
              y = (position.y + velocity.y).mod(dimensions.y),
            )
        )

      private fun drawGrid(): Array<CharArray> {
        val (numRows, numColumns) = dimensions.y to dimensions.x
        val grid = Array(numRows) { _ -> CharArray(numColumns) }

        val gridPositions = positions.map { it.toGridCoordinate() }
        for (row in grid.indices) {
          for (col in grid[row].indices) {
            grid[row][col] =
              if (GridCoordinate(row, col) in gridPositions) {
                ROBOT
              } else {
                EMPTY
              }
          }
        }
        return grid
      }

      fun saveGridImage(outputPath: Path, scale: Int = 1) {
        val grid = drawGrid()
        val height = grid.size
        val width = grid[0].size

        // Create a buffered image with the calculated dimensions
        // We multiply by scale to make the image visible if the array is small
        val image = BufferedImage(width * scale, height * scale, BufferedImage.TYPE_INT_RGB)
        val graphics = image.createGraphics()

        for (y in 0 until height) {
          for (x in 0 until width) {
            graphics.color =
              when (grid[y][x]) {
                ROBOT -> Color.GREEN
                else -> Color.BLACK
              }
            // Draw a rectangle for the pixel (scaled up)
            graphics.fillRect(x * scale, y * scale, scale, scale)
          }
        }
        graphics.dispose() // Release system resources

        // Write the image to a file
        val file = outputPath.toFile()
        val formatName = outputPath.extension
        ImageIO.write(image, formatName, file)
      }
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
data class Position(val x: Int, val y: Int) {
  fun toGridCoordinate() = GridCoordinate(row = y, column = x)
}

/**
 * Each robot's velocity is given as v=x,y where x and y are given in tiles per second. Positive x
 * means the robot is moving to the right, and positive y means the robot is moving down.
 */
data class Velocity(val x: Int, val y: Int)

data class Dimensions(val x: Int, val y: Int)

val DEFAULT_DIMENSIONS = Dimensions(x = 101, y = 103)

// Indicates the presence of a robot on the grid
const val ROBOT = 'R'
const val EMPTY = ' '
