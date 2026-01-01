package day13

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.readInputText

class Day13Test {
  private val part1 = Day13.Part1()
  private val part2 = Day13.Part2()

  @Test
  fun `part 1 sample input - machine 1`() {
    part1.solve(SAMPLE_INPUT_MACHINE_1).shouldBe(280L)
  }

  @Test
  fun `part 1 sample input - machine 2`() {
    part1.solve(SAMPLE_INPUT_MACHINE_2).shouldBe(0L)
  }

  @Test
  fun `part 1 sample input - machine 3`() {
    part1.solve(SAMPLE_INPUT_MACHINE_3).shouldBe(200L)
  }

  @Test
  fun `part 1 sample input - machine 4`() {
    part1.solve(SAMPLE_INPUT_MACHINE_4).shouldBe(0L)
  }

  @Test
  fun `part 1 sample input`() {
    val input = readInputText("day13/day13-sample.txt")
    part1.solve(input).shouldBe(200L + 280L)
  }

  @Test
  fun `part 1 input`() {
    val input = readInputText("day13/day13-input.txt")
    part1.solve(input).shouldBe(29517L)
  }

  @Test
  fun `part 2 sample input - machine 1`() {
    part2.solve(SAMPLE_INPUT_MACHINE_1).shouldBe(0L)
  }

  @Test
  fun `part 2 sample input - machine 2`() {
    val aPresses = 118679050709L
    val bPresses = 103199174542L
    val minCost = (aPresses * 3L) + bPresses
    part2.solve(SAMPLE_INPUT_MACHINE_2).shouldBe(minCost)
  }

  @Test
  fun `part 2 sample input - machine 3`() {
    part2.solve(SAMPLE_INPUT_MACHINE_3).shouldBe(0L)
  }

  @Test
  fun `part 2 sample input - machine 4`() {
    part2.solve(SAMPLE_INPUT_MACHINE_4).shouldBe((102851800151L * 3L) + 107526881786L)
  }

  @Test
  fun `part 2 sample input`() {
    val input = readInputText("day13/day13-sample.txt")
    val machine2Tokens = 459236326669L
    val machine4Tokens = 416082282239L
    part2.solve(input).shouldBe(machine2Tokens + machine4Tokens)
  }

  @Test
  fun `part 2 input`() {
    val input = readInputText("day13/day13-input.txt")
    part2.solve(input).shouldBe(103570327981381)
  }

  companion object {
    val SAMPLE_INPUT_MACHINE_1 =
      """
      Button A: X+94, Y+34
      Button B: X+22, Y+67
      Prize: X=8400, Y=5400
      """
        .trimIndent()
    val SAMPLE_INPUT_MACHINE_2 =
      """
      Button A: X+26, Y+66
      Button B: X+67, Y+21
      Prize: X=12748, Y=12176
      """
        .trimIndent()

    val SAMPLE_INPUT_MACHINE_3 =
      """
      Button A: X+17, Y+86
      Button B: X+84, Y+37
      Prize: X=7870, Y=6450
      """
        .trimIndent()
    val SAMPLE_INPUT_MACHINE_4 =
      """
      Button A: X+69, Y+23
      Button B: X+27, Y+71
      Prize: X=18641, Y=10279
      """
        .trimIndent()
  }
}
