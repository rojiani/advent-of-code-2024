package day15

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.readInputText

class Day15Part2Test {
  private val part2 = Day15Part2()

  @Test
  fun `part 2 example input - grid after moves`() {
    // Initial state:
    part2
      .state(part2.gridAfterMoves(SMALL_EXAMPLE, numMoves = 0))
      .shouldBe(
        """
        ##############
        ##......##..##
        ##..........##
        ##....[][]@.##
        ##....[]....##
        ##..........##
        ##############
        """
          .trimIndent()
      )

    // Move <:
    part2
      .state(part2.gridAfterMoves(SMALL_EXAMPLE, numMoves = 1))
      .shouldBe(
        """
        ##############
        ##......##..##
        ##..........##
        ##...[][]@..##
        ##....[]....##
        ##..........##
        ##############
        """
          .trimIndent()
      )
    // Move v:
    part2
      .state(part2.gridAfterMoves(SMALL_EXAMPLE, numMoves = 2))
      .shouldBe(
        """
        ##############
        ##......##..##
        ##..........##
        ##...[][]...##
        ##....[].@..##
        ##..........##
        ##############
        """
          .trimIndent()
      )

    // Move v:
    part2
      .state(part2.gridAfterMoves(SMALL_EXAMPLE, numMoves = 3))
      .shouldBe(
        """
        ##############
        ##......##..##
        ##..........##
        ##...[][]...##
        ##....[]....##
        ##.......@..##
        ##############
        """
          .trimIndent()
      )

    // Move <:
    part2
      .state(part2.gridAfterMoves(SMALL_EXAMPLE, numMoves = 4))
      .shouldBe(
        """
        ##############
        ##......##..##
        ##..........##
        ##...[][]...##
        ##....[]....##
        ##......@...##
        ##############
        """
          .trimIndent()
      )

    // Move <:
    part2
      .state(part2.gridAfterMoves(SMALL_EXAMPLE, numMoves = 5))
      .shouldBe(
        """
        ##############
        ##......##..##
        ##..........##
        ##...[][]...##
        ##....[]....##
        ##.....@....##
        ##############
        """
          .trimIndent()
      )

    //     Move ^:
    part2
      .state(part2.gridAfterMoves(SMALL_EXAMPLE, numMoves = 6))
      .shouldBe(
        """
        ##############
        ##......##..##
        ##...[][]...##
        ##....[]....##
        ##.....@....##
        ##..........##
        ##############
        """
          .trimIndent()
      )

    // Move ^:
    part2
      .state(part2.gridAfterMoves(SMALL_EXAMPLE, numMoves = 7))
      .shouldBe(
        """
        ##############
        ##......##..##
        ##...[][]...##
        ##....[]....##
        ##.....@....##
        ##..........##
        ##############
        """
          .trimIndent()
      )
    // Move <:
    part2
      .state(part2.gridAfterMoves(SMALL_EXAMPLE, numMoves = 8))
      .shouldBe(
        """
        ##############
        ##......##..##
        ##...[][]...##
        ##....[]....##
        ##....@.....##
        ##..........##
        ##############
        """
          .trimIndent()
      )

    // Move <:
    part2
      .state(part2.gridAfterMoves(SMALL_EXAMPLE, numMoves = 9))
      .shouldBe(
        """
        ##############
        ##......##..##
        ##...[][]...##
        ##....[]....##
        ##...@......##
        ##..........##
        ##############
        """
          .trimIndent()
      )

    // Move ^:
    part2
      .state(part2.gridAfterMoves(SMALL_EXAMPLE, numMoves = 10))
      .shouldBe(
        """
        ##############
        ##......##..##
        ##...[][]...##
        ##...@[]....##
        ##..........##
        ##..........##
        ##############
        """
          .trimIndent()
      )

    // Move ^:
    part2
      .state(part2.gridAfterMoves(SMALL_EXAMPLE, numMoves = 11))
      .shouldBe(
        """
        ##############
        ##...[].##..##
        ##...@.[]...##
        ##....[]....##
        ##..........##
        ##..........##
        ##############
        """
          .trimIndent()
      )
  }

  @Test
  fun `part 2 sample input - grid after all moves`() {
    val input = readInputText("day15-sample.txt")
    part2
      .state(part2.gridAfterMoves(input))
      .shouldBe(
        """
        ####################
        ##[].......[].[][]##
        ##[]...........[].##
        ##[]........[][][]##
        ##[]......[]....[]##
        ##..##......[]....##
        ##..[]............##
        ##..@......[].[][]##
        ##......[][]..[]..##
        ####################
        """
          .trimIndent()
      )
  }

  // https://www.reddit.com/r/adventofcode/comments/1heoj7f/2024_day_15_part_2_more_sample_inputs_to_catch/
  @Test
  fun `part 2 reddit input 1`() {
    val input =
      """
      #######
      #.....#
      #.OO@.#
      #.....#
      #######

      <<
      """
        .trimIndent()
    part2.solve(input).shouldBe(406L)
  }

  @Test
  fun `part 2 reddit input 2`() {
    val input =
      """
      #######
      #.....#
      #.O#..#
      #..O@.#
      #.....#
      #######

      <v<<^
      """
        .trimIndent()
    part2.solve(input).shouldBe(509L)
  }

  @Test
  fun `part 2 reddit input 3`() {
    val input =
      """
      ########
      #......#
      #..O...#
      #.O....#
      #..O...#
      #@O....#
      #......#
      ########

      >>^<^>^^>>>>v<<^<<<vvvvv>>^
      """
        .trimIndent()
    part2.solve(input).shouldBe(1020L)
  }

  @Test
  fun `part 2 reddit input 4`() {
    val input =
      """
      #######
      #.....#
      #.O.O@#
      #..O..#
      #..O..#
      #.....#
      #######

      <v<<>vv<^^
      """
        .trimIndent()
    part2.solve(input).shouldBe(822L)
  }

  @Test
  fun `part 2 reddit input 5`() {
    val input =
      """
      ########
      #......#
      #..O...#
      #.O....#
      #..O...#
      #@O....#
      #......#
      ########

      >>^<^>^^>>>>v<<^<<<vvvvv>>
      """
        .trimIndent()
    part2.solve(input).shouldBe(1420L)
  }

  @Test
  fun `part 2 reddit input 6`() {
    val input =
      """
      ########
      #......#
      #..O...#
      #.O....#
      #..O...#
      #@O....#
      #......#
      ########

      >>^<^>^^>>>>v<<^<<<vvvvv>>^
      """
        .trimIndent()
    part2.solve(input).shouldBe(1020L)
  }

  @Test
  fun `part 2 reddit input 7`() {
    val input =
      """
      ########
      #......#
      ##@.O..#
      #...O..#
      #.#.O..#
      #...O..#
      #......#
      ########

      >>>vv><^^^>vv
      """
        .trimIndent()
    part2.solve(input).shouldBe(1833)
  }

  @Test
  fun `part 2 reddit input 8`() {
    val input =
      """
      ########
      #......#
      #OO....#
      #.O....#
      #.O....#
      ##O....#
      #O..O@.#
      #......#
      ########

      <^^<<>^^^<v
      """
        .trimIndent()
    part2.solve(input).shouldBe(2827)
  }

  @Test
  fun `part 2 sample input`() {
    val input = readInputText("day15-sample.txt")
    part2.solve(input).shouldBe(9021L)
  }

  @Test
  fun `part 2 input`() {
    val input = readInputText("day15-input.txt")
    part2.solve(input).shouldBe(1392847L)
  }

  companion object {
    private val SMALL_EXAMPLE =
      """
      #######
      #...#.#
      #.....#
      #..OO@#
      #..O..#
      #.....#
      #######

      <vv<<^^<<^^
      """
        .trimIndent()
  }
}
