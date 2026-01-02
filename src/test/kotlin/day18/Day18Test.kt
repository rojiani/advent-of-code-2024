package day18

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.readInputText

class Day18Test {
  private val part1 = Day18.Part1()
  private val part2 = Day18.Part2()

  @Test
  fun `part 1 sample input first 12`() {
    val input = readInputText("day18/day18-sample.txt")
    part1.solve(input, maxRowOrColumnIndex = 6, numBytes = 12).shouldBe(22)
  }

  @Test
  fun `part 1 input`() {
    val input = readInputText("day18/day18-input.txt")
    part1.solve(input, maxRowOrColumnIndex = 70, numBytes = 1024).shouldBe(280)
  }

  @Test
  fun `part 2 sample input`() {
    val input = readInputText("day18/day18-sample.txt")
    part2.solve(input, maxRowOrColumnIndex = 6).shouldBe("6,1")
  }

  @Test
  fun `part 2 input`() {
    val input = readInputText("day18/day18-input.txt")
    part2.solve(input, maxRowOrColumnIndex = 70).shouldBe("28,56")
  }
}
