package day03

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.readInputText

class Day03Test {
  private val part1 = Day03.Part1()
  private val part2 = Day03.Part2()

  @Test
  fun `part 1 sample input`() {
    val input = readInputText("day03-part1-sample.txt")
    part1.solve(input).shouldBe(161)
  }

  @Test
  fun `part 1 input`() {
    val input = readInputText("day03-input.txt")
    part1.solve(input).shouldBe(166630675)
  }

  @Test
  fun `part 2 sample input`() {
    val input = readInputText("day03-part2-sample.txt")
    part2.solve(input).shouldBe(48)
  }

  @Test
  fun `part 2 input`() {
    val input = readInputText("day03-input.txt")
    part2.solve(input).shouldBe(93465710)
  }
}
