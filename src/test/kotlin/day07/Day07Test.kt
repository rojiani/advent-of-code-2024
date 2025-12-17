package day07

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.readInputText

class Day07Test {
  private val part1 = Day07.Part1()
  private val part2 = Day07.Part2()

  @Test
  fun `part 1 sample input`() {
    val input = readInputText("day07-sample.txt")
    part1.solve(input).shouldBe(3749L)
  }

  @Test
  fun `part 1 input`() {
    val input = readInputText("day07-input.txt")
    part1.solve(input).shouldBe(2941973819040L)
  }

  @Test
  fun `part 2 sample input`() {
    val input = readInputText("day07-sample.txt")
    part2.solve(input).shouldBe(11387)
  }

  @Test
  fun `part 2 input`() {
    val input = readInputText("day07-input.txt")
    part2.solve(input).shouldBe(249943041417600L)
  }
}
