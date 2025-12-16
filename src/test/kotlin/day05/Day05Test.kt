package day05

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.readInputText

class Day05Test {
  private val part1 = Day05.Part1()
  private val part2 = Day05.Part2()

  @Test
  fun `part 1 sample input`() {
    val input = readInputText("day05-sample.txt")
    part1.solve(input).shouldBe(143)
  }

  @Test
  fun `part 1 input`() {
    val input = readInputText("day05-input.txt")
    part1.solve(input).shouldBe(7307)
  }

  @Test
  fun `part 2 sample input`() {
    val input = readInputText("day05-sample.txt")
    part2.solve(input).shouldBe(123)
  }

  @Test
  fun `part 2 input`() {
    val input = readInputText("day05-input.txt")
    part2.solve(input).shouldBe(4713)
  }
}
