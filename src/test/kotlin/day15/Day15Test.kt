package day15

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.readInputText

class Day15Test {
  private val part1 = Day15.Part1()
  private val part2 = Day15.Part2()

  @Test
  fun `part 1 small example - grid`() {
    // Move <:
    part1
      .gridAfterMoves(SMALL_EXAMPLE, numMoves = 1)
      .state()
      .shouldBe(
        """
        ########
        #..O.O.#
        ##@.O..#
        #...O..#
        #.#.O..#
        #...O..#
        #......#
        ########
        """
          .trimIndent()
      )

    // Move ^:
    part1
      .gridAfterMoves(SMALL_EXAMPLE, numMoves = 2)
      .state()
      .shouldBe(
        """
        ########
        #.@O.O.#
        ##..O..#
        #...O..#
        #.#.O..#
        #...O..#
        #......#
        ########
        """
          .trimIndent()
      )

    // Move ^:
    part1
      .gridAfterMoves(SMALL_EXAMPLE, numMoves = 3)
      .state()
      .shouldBe(
        """
        ########
        #.@O.O.#
        ##..O..#
        #...O..#
        #.#.O..#
        #...O..#
        #......#
        ########
        """
          .trimIndent()
      )

    // Move >:
    part1
      .gridAfterMoves(SMALL_EXAMPLE, numMoves = 4)
      .state()
      .shouldBe(
        """
        ########
        #..@OO.#
        ##..O..#
        #...O..#
        #.#.O..#
        #...O..#
        #......#
        ########
        """
          .trimIndent()
      )

    // Move >:
    part1
      .gridAfterMoves(SMALL_EXAMPLE, numMoves = 5)
      .state()
      .shouldBe(
        """
        ########
        #...@OO#
        ##..O..#
        #...O..#
        #.#.O..#
        #...O..#
        #......#
        ########
        """
          .trimIndent()
      )

    // Move >:
    part1
      .gridAfterMoves(SMALL_EXAMPLE, numMoves = 6)
      .state()
      .shouldBe(
        """
        ########
        #...@OO#
        ##..O..#
        #...O..#
        #.#.O..#
        #...O..#
        #......#
        ########
        """
          .trimIndent()
      )

    // Move v:
    part1
      .gridAfterMoves(SMALL_EXAMPLE, numMoves = 7)
      .state()
      .shouldBe(
        """
        ########
        #....OO#
        ##..@..#
        #...O..#
        #.#.O..#
        #...O..#
        #...O..#
        ########
        """
          .trimIndent()
      )

    // Move v:
    part1
      .gridAfterMoves(SMALL_EXAMPLE, numMoves = 8)
      .state()
      .shouldBe(
        """
        ########
        #....OO#
        ##..@..#
        #...O..#
        #.#.O..#
        #...O..#
        #...O..#
        ########
        """
          .trimIndent()
      )

    // Move <:
    part1
      .gridAfterMoves(SMALL_EXAMPLE, numMoves = 9)
      .state()
      .shouldBe(
        """
        ########
        #....OO#
        ##.@...#
        #...O..#
        #.#.O..#
        #...O..#
        #...O..#
        ########
        """
          .trimIndent()
      )

    // Move v:
    part1
      .gridAfterMoves(SMALL_EXAMPLE, numMoves = 10)
      .state()
      .shouldBe(
        """
        ########
        #....OO#
        ##.....#
        #..@O..#
        #.#.O..#
        #...O..#
        #...O..#
        ########
        """
          .trimIndent()
      )

    // Move >:
    part1
      .gridAfterMoves(SMALL_EXAMPLE, numMoves = 11)
      .state()
      .shouldBe(
        """
        ########
        #....OO#
        ##.....#
        #...@O.#
        #.#.O..#
        #...O..#
        #...O..#
        ########
        """
          .trimIndent()
      )

    // Move >:
    part1
      .gridAfterMoves(SMALL_EXAMPLE, numMoves = 12)
      .state()
      .shouldBe(
        """
        ########
        #....OO#
        ##.....#
        #....@O#
        #.#.O..#
        #...O..#
        #...O..#
        ########
        """
          .trimIndent()
      )

    // Move v:
    part1
      .gridAfterMoves(SMALL_EXAMPLE, numMoves = 13)
      .state()
      .shouldBe(
        """
        ########
        #....OO#
        ##.....#
        #.....O#
        #.#.O@.#
        #...O..#
        #...O..#
        ########
        """
          .trimIndent()
      )

    // Move <:
    part1
      .gridAfterMoves(SMALL_EXAMPLE, numMoves = 14)
      .state()
      .shouldBe(
        """
        ########
        #....OO#
        ##.....#
        #.....O#
        #.#O@..#
        #...O..#
        #...O..#
        ########
        """
          .trimIndent()
      )

    // Move <:
    part1
      .gridAfterMoves(SMALL_EXAMPLE, numMoves = 15)
      .state()
      .shouldBe(
        """
        ########
        #....OO#
        ##.....#
        #.....O#
        #.#O@..#
        #...O..#
        #...O..#
        ########
        """
          .trimIndent()
      )
  }

  @Test
  fun `part 1 sample input - grid after all moves`() {
    val input = readInputText("day15-sample.txt")
    part1
      .gridAfterMoves(input)
      .state()
      .shouldBe(
        """
        ##########
        #.O.O.OOO#
        #........#
        #OO......#
        #OO@.....#
        #O#.....O#
        #O.....OO#
        #O.....OO#
        #OO....OO#
        ##########
        """
          .trimIndent()
      )
  }

  @Test
  fun `part 1 small example`() {
    part1.solve(SMALL_EXAMPLE).shouldBe(2028L)
  }

  @Test
  fun `part 1 sample input`() {
    val input = readInputText("day15-sample.txt")
    part1.solve(input).shouldBe(10092L)
  }

  @Test
  fun `part 1 input`() {
    val input = readInputText("day15-input.txt")
    part1.solve(input).shouldBe(1371036L)
  }

  companion object {
    private val SMALL_EXAMPLE =
      """
      ########
      #..O.O.#
      ##@.O..#
      #...O..#
      #.#.O..#
      #...O..#
      #......#
      ########

      <^^>>>vv<v>>v<<
      """
        .trimIndent()
  }
}
