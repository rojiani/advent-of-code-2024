package day12

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import utils.readInputText

class Day12Test {
  private val part1 = Day12.Part1()

  @Test
  fun `getAllRegions - simple example`() {
    val grid = parseInput(SAMPLE_INPUT_1)
    val regionsByPlant: Map<Plant, List<Region>> = part1.getAllRegions(grid)
    regionsByPlant.keys.shouldContainExactlyInAnyOrder(
      Plant('A'),
      Plant('B'),
      Plant('C'),
      Plant('D'),
      Plant('E'),
    )

    val plantARegions = regionsByPlant[Plant('A')].shouldNotBeNull()
    plantARegions.shouldHaveSize(1)
    val plantARegion = plantARegions.single()
    plantARegion.apply {
      area shouldBe 4
      perimeter shouldBe 10
    }

    val plantBRegions = regionsByPlant[Plant('B')].shouldNotBeNull()
    plantBRegions.shouldHaveSize(1)
    val plantBRegion = plantBRegions.single()
    plantBRegion.apply {
      area shouldBe 4
      perimeter shouldBe 8
    }

    val plantCRegions = regionsByPlant[Plant('C')].shouldNotBeNull()
    plantCRegions.shouldHaveSize(1)
    val plantCRegion = plantCRegions.single()
    plantCRegion.apply {
      area shouldBe 4
      perimeter shouldBe 10
    }

    val plantDRegions = regionsByPlant[Plant('D')].shouldNotBeNull()
    plantDRegions.shouldHaveSize(1)
    val plantDRegion = plantDRegions.single()
    plantDRegion.apply {
      area shouldBe 1
      perimeter shouldBe 4
    }

    val plantERegions = regionsByPlant[Plant('E')].shouldNotBeNull()
    plantERegions.shouldHaveSize(1)
    val plantERegion = plantERegions.single()
    plantERegion.apply {
      area shouldBe 3
      perimeter shouldBe 8
    }
  }

  @Test
  fun `getAllRegions - nested example`() {
    val grid = parseInput(SAMPLE_INPUT_2)
    val regionsByPlant: Map<Plant, List<Region>> = part1.getAllRegions(grid)
    regionsByPlant.keys.shouldContainExactlyInAnyOrder(Plant('O'), Plant('X'))

    // The above map contains five regions, one containing all of the O garden plots, and the other
    // four each containing a single X plot.

    // The four X regions each have area 1 and perimeter 4.
    val plantXRegions = regionsByPlant[Plant('X')].shouldNotBeNull()
    plantXRegions.shouldHaveSize(4)
    for (xRegion in plantXRegions) {
      xRegion.area shouldBe 1
      xRegion.perimeter shouldBe 4
      xRegion.fencePrice shouldBe 4L
    }

    // The region containing 21 type O plants is more complicated; in addition to its outer edge
    // contributing a perimeter of 20, its boundary with each X region contributes an additional 4
    // to its perimeter, for a total perimeter of 36.
    val plantORegions = regionsByPlant[Plant('O')].shouldNotBeNull()
    plantORegions.shouldHaveSize(1)
    val plantORegion = plantORegions.single()
    plantORegion.apply {
      area shouldBe 21
      perimeter shouldBe 36
      fencePrice shouldBe 756L
    }
  }

  @Test
  fun `part 1 example 1`() {
    part1.solve(SAMPLE_INPUT_1) shouldBe 140L
  }

  @Test
  fun `part 1 example 2`() {
    part1.solve(SAMPLE_INPUT_2) shouldBe 772L
  }

  @Test
  fun `part 1 sample`() {
    val input = readInputText("day12-sample.txt")
    part1.solve(input) shouldBe 1930L
  }

  @Test
  fun `part 1 input`() {
    val input = readInputText("day12-input.txt")
    part1.solve(input) shouldBe 1431316L
  }

  companion object {
    private val SAMPLE_INPUT_1 =
      """
      AAAA
      BBCD
      BBCC
      EEEC
      """
        .trimIndent()

    private val SAMPLE_INPUT_2 =
      """
      OOOOO
      OXOXO
      OOOOO
      OXOXO
      OOOOO
      """
        .trimIndent()
  }
}
