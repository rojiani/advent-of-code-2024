package day10

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import utils.readInputText

class Day10Test {
  private val part1 = Day10.Part1()
  private val part2 = Day10.Part2()

  @Test
  fun `part 1 example 1`() {
    val input =
      """
      |...0...
      |...1...
      |...2...
      |6543456
      |7.....7
      |8.....8
      |9.....9
      """
        .trimMargin()
    part1.solve(input).shouldBe(2)
  }

  @Test
  fun `part 1 example 2`() {
    val input =
      """
      |..90..9
      |...1.98
      |...2..7
      |6543456
      |765.987
      |876....
      |987....
      """
        .trimMargin()
    part1.solve(input).shouldBe(4)
  }

  @Test
  fun `part 1 example 3`() {
    val input =
      """
      |10..9..
      |2...8..
      |3...7..
      |4567654
      |...8..3
      |...9..2
      |.....01
      """
        .trimMargin()
    part1.solve(input).shouldBe(1 + 2)
  }

  @Test
  fun `part 1 example 4`() {
    val input =
      """
      |89010123
      |78121874
      |87430965
      |96549874
      |45678903
      |32019012
      |01329801
      |10456732
      """
        .trimMargin()

    // This larger example has 9 trailheads. Considering the trailheads in reading order, they have
    // scores of 5, 6, 5, 3, 1, 3, 5, 3, and 5. Adding these scores together, the sum of the scores
    // of all trailheads is 36.
    part1.solve(input).shouldBe(36)
  }

  @Test
  fun `part 1 sample input`() {
    val input = readInputText("day10-sample.txt")
    part1.solve(input).shouldBe(36)
  }

  @Test
  fun `part 1 input`() {
    val input = readInputText("day10-input.txt")
    part1.solve(input).shouldBe(782)
  }

  @Disabled("TODO")
  @Test
  fun `part 2 sample input`() {
    val input = readInputText("day10-sample.txt")
    part2.solve(input).shouldBe(0)
  }

  @Disabled("TODO")
  @Test
  fun `part 2 input`() {
    val input = readInputText("day10-input.txt")
    part2.solve(input).shouldBe(0)
  }
}
