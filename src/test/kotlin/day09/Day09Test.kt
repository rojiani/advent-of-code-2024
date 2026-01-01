package day09

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.readInputText

class Day09Test {
  private val part1 = Day09.Part1()
  private val part2 = Day09.Part2()

  @Test
  fun `part 1 sample input`() {
    val input = readInputText("day09/day09-sample.txt")
    part1.solve(input).shouldBe(1928L)
  }

  @Test
  fun `part 1 input`() {
    val input = readInputText("day09/day09-input.txt")
    part1.solve(input).shouldBe(6242766523059L)
  }

  @Test
  fun `part 2 sample input`() {
    val input = readInputText("day09/day09-sample.txt")
    part2.solve(input).shouldBe(2858L)
  }

  @Test
  fun `part 2 input`() {
    val input = readInputText("day09/day09-input.txt")
    part2.solve(input).shouldBe(6272188244509L)
  }
}
