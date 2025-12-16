package day06

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.readInputText

class Day06Test {
  private val part1 = Day06.Part1()
  private val part2 = Day06.Part2()

  @Test
  fun `part 1 sample input`() {
    val input = readInputText("day06-sample.txt")
    part1.solve(input).shouldBe(41)
  }

  @Test
  fun `part 1 input`() {
    val input = readInputText("day06-input.txt")
    part1.solve(input).shouldBe(5269)
  }
}
