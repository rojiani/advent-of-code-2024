package day16

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import utils.readInputText

class Day16Test {
  private val part1 = Day16.Part1()
  private val part2 = Day16.Part2()

  @Test
  fun `part 1 small maze`() {
    val input =
      """
      ######
      #S...#
      #.#.##
      #..E.#
      ######
      """
        .trimIndent()
    // start: (1, 1), cost = 0
    // >: (1, 2), cost = 1
    // >, (1, 3), cost = 2
    // clockwise: (1, 3), cost = 1002
    // v: (2, 3), cost = 1003,
    // v: (3, 3), cost = 1004
    part1.solve(input).shouldBe(1004)
  }

  @Test
  fun `part 1 small maze 2`() {
    val input =
      """
      ######
      #S...#
      #.####
      #..E.#
      ######
      """
        .trimIndent()
    // turn, v, v, turn, >, >
    part1.solve(input).shouldBe(2004)
  }

  @Test
  fun `part 1 reddit input`() {
    val input =
      """
      #######E#######
      #...#...#######
      #.#...#.......#
      #.###########.#
      #S............#
      ###############
      """
        .trimIndent()
    part1.solve(input).shouldBe(3022)
  }

  @Test
  fun `part 1 sample input 1`() {
    val input = readInputText("day16-sample-1.txt")
    part1.solve(input).shouldBe(7036)
  }

  @Test
  fun `part 1 sample input 2`() {
    val input = readInputText("day16-sample-2.txt")
    part1.solve(input).shouldBe(11048)
  }

  @Test
  fun `part 1 input`() {
    val input = readInputText("day16-input.txt")
    part1.solve(input).shouldBe(135536)
  }

  @Disabled("Not yet implemented")
  @Test
  fun `part 2 sample input`() {
    val input = readInputText("day16-sample.txt")
    part2.solve(input).shouldBe(0)
  }

  @Disabled("Not yet implemented")
  @Test
  fun `part 2 input`() {
    val input = readInputText("day16-input.txt")
    part2.solve(input).shouldBe(0)
  }
}
