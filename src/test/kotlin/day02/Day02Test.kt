package day02

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.readInputText

class Day02Test {
  private val part1 = Day02.Part1()
  private val part2 = Day02.Part2()

  @Test
  fun `part 1 sample input`() {
    val input = readInputText("day02-sample.txt")
    part1.solve(input).shouldBe(2)
  }

  @Test
  fun `part 1 input`() {
    val input = readInputText("day02-input.txt")
    part1.solve(input).shouldBe(585)
  }

  @Test
  fun `part 2 sample input`() {
    val input = readInputText("day02-sample.txt")
    part2.solve(input).shouldBe(4)
  }

  @Test
  fun `part 2 input`() {
    val input = readInputText("day02-input.txt")
    part2.solve(input).shouldBe(626)
  }
}
