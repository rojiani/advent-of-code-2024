package day15

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.readInputText

class Day15Part1Test {
  private val part1 = Day15Part1()

  @Test
  fun `small example - grid`() {
    // Move <:
    part1
      .state(part1.gridAfterMoves(SMALL_EXAMPLE, numMoves = 1))
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
      .state(part1.gridAfterMoves(SMALL_EXAMPLE, numMoves = 2))
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
      .state(part1.gridAfterMoves(SMALL_EXAMPLE, numMoves = 3))
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
      .state(part1.gridAfterMoves(SMALL_EXAMPLE, numMoves = 4))
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
      .state(part1.gridAfterMoves(SMALL_EXAMPLE, numMoves = 5))
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
      .state(part1.gridAfterMoves(SMALL_EXAMPLE, numMoves = 6))
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
      .state(part1.gridAfterMoves(SMALL_EXAMPLE, numMoves = 7))
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
      .state(part1.gridAfterMoves(SMALL_EXAMPLE, numMoves = 8))
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
      .state(part1.gridAfterMoves(SMALL_EXAMPLE, numMoves = 9))
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
      .state(part1.gridAfterMoves(SMALL_EXAMPLE, numMoves = 10))
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
      .state(part1.gridAfterMoves(SMALL_EXAMPLE, numMoves = 11))
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
      .state(part1.gridAfterMoves(SMALL_EXAMPLE, numMoves = 12))
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
      .state(part1.gridAfterMoves(SMALL_EXAMPLE, numMoves = 13))
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
      .state(part1.gridAfterMoves(SMALL_EXAMPLE, numMoves = 14))
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
      .state(part1.gridAfterMoves(SMALL_EXAMPLE, numMoves = 15))
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
  fun `sample input - grid after all moves`() {
    val input = readInputText("day15/day15-sample.txt")
    part1
      .state(part1.gridAfterMoves(input))
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
  fun `small example`() {
    part1.solve(SMALL_EXAMPLE).shouldBe(2028L)
  }

  @Test
  fun `sample input`() {
    val input = readInputText("day15/day15-sample.txt")
    part1.solve(input).shouldBe(10092L)
  }

  @Test
  fun input() {
    val input = readInputText("day15/day15-input.txt")
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
