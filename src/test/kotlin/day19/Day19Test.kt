package day19

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.readInputText

class Day19Test {
  private val part1 = Day19.Part1()
  private val part2 = Day19.Part2()

  @Test
  fun `part 1 sample input`() {
    val input = readInputText("day19/day19-sample.txt")
    part1.solve(input).shouldBe(6)
  }

  @Test
  fun `part 1 input`() {
    val input = readInputText("day19/day19-input.txt")
    part1.solve(input).shouldBe(236)
  }
}
