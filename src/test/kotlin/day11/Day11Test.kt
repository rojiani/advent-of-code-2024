package day11

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.readInputText

class Day11Test {
  private val part1 = Day11.Part1()
  private val part2 = Day11.Part2()

  @Test
  fun `part 1 example 1`() {
    part1.solve("0 1 10 99 999", blinks = 1).shouldBe(7)
  }

  @Test
  fun `part 1 example 2`() {
    part1.solve("125 17", blinks = 6).shouldBe(22)
  }

  @Test
  fun `part 1 sample input`() {
    val input = readInputText("day11/day11-sample.txt")
    part1.solve(input, blinks = 25).shouldBe(55312)
  }

  @Test
  fun `part 1 input`() {
    val input = readInputText("day11/day11-input.txt")
    part1.solve(input, blinks = 25).shouldBe(200446)
  }

  @Test
  fun `part 2 example 1`() {
    part2.solve("0 1 10 99 999", blinks = 1).shouldBe(7L)
  }

  @Test
  fun `part 2 example 2`() {
    part2.solve("125", blinks = 1).shouldBe(1) // 25300L0
    part2.solve("125", blinks = 2).shouldBe(2) // 253 L0
    part2.solve("125", blinks = 3).shouldBe(2) // 512072 L1
    part2.solve("125", blinks = 4).shouldBe(3) // 512 72 202L4
    part2.solve("125", blinks = 5).shouldBe(5) // 1036288 7 2 20 2L4
    part2.solve("125 17", blinks = 1).shouldBe(3L)
    part2.solve("253000 1 7", blinks = 1).shouldBe(4L)
    part2.solve("125 17", blinks = 2).shouldBe(4L)
    part2.solve("253 0 2024 14168", blinks = 1).shouldBe(5L)
    part2.solve("125 17", blinks = 3).shouldBe(5L)
    part2.solve("125 17", blinks = 4).shouldBe(9L)
    part2.solve("512 72 2024 2 0 2 4 2867 6032", blinks = 1).shouldBe(13L)
    part2.solve("125 17", blinks = 5).shouldBe(13L)
    part2.solve("125 17", blinks = 6).shouldBe(22L)
  }

  @Test
  fun `part 2 sample input`() {
    val input = readInputText("day11/day11-sample.txt")
    part2.solve(input, blinks = 25).shouldBe(55312L)
  }

  @Test
  fun `part 2 input - 25 blinks`() {
    val input = readInputText("day11/day11-input.txt")
    part2.solve(input, blinks = 25).shouldBe(200446L)
  }

  @Test
  fun `part 2 input`() {
    val input = readInputText("day11/day11-input.txt")
    part2.solve(input).shouldBe(238317474993392L)
  }
}
