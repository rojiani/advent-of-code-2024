package day14

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import utils.readInputText

class Day14Test {
  private val part1 = Day14.Part1()
  private val part2 = Day14.Part2()

  @Test
  fun `part 1 example - single robot - getAllPositions`() {
    val input = "p=2,4 v=2,-3"
    val robots = parseInput(input)

    val expectedPositionAtSeconds =
      mapOf(
        0 to Position(2, 4),
        1 to Position(4, 1),
        2 to Position(6, 5),
        3 to Position(8, 2),
        4 to Position(10, 6),
        5 to Position(1, 3),
      )

    for (seconds in 0..5) {
      val expectedPosition = expectedPositionAtSeconds.getValue(seconds)
      part1
        .getAllFinalPositions(robots, dimensions = Dimensions(x = 11, y = 7), seconds = seconds)
        .shouldContainExactly(expectedPosition)
    }
  }

  @Test
  fun `part 1 sample input - final positions`() {
    val input = readInputText("day14-sample.txt")
    val robots = parseInput(input)
    part1
      .getAllFinalPositions(robots, dimensions = Dimensions(x = 11, y = 7), seconds = 100)
      .shouldContainExactlyInAnyOrder(
        Position(6, 0),
        Position(6, 0),
        Position(9, 0),
        Position(0, 2),
        Position(1, 3),
        Position(2, 3),
        Position(5, 4),
        Position(3, 5),
        Position(4, 5),
        Position(4, 5),
        Position(1, 6),
        Position(6, 6),
      )
  }

  @Test
  fun `part 1 sample input`() {
    val input = readInputText("day14-sample.txt")
    val robots = parseInput(input)
    part1.solve(robots, dimensions = Dimensions(x = 11, y = 7), seconds = 100).shouldBe(12)
  }

  @Test
  fun `part 1 input`() {
    val input = readInputText("day14-input.txt")
    part1.solve(input).shouldBe(225521010)
  }

  @Disabled("The code generates images that need to be manually inspected")
  @Test
  fun `part 2 input`() {
    val input = readInputText("day14-input.txt")
    part2.solve(input).shouldBe(7774)
  }
}
