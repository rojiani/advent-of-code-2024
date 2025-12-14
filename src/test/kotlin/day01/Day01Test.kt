package day01

import utils.readInputText
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day01Test {
    private val part1 = Day01.Part1()
    private val part2 = Day01.Part2()

    @Test
    fun `part 1 sample input`() {
        val input = readInputText("day01-sample.txt")
        part1.solve(input).shouldBe(11)
    }

    @Test
    fun `part 1 input`() {
        val input = readInputText("day01-input.txt")
        part1.solve(input).shouldBe(1834060)
    }

    @Test
    fun `part 2 sample input`() {
        val input = readInputText("day01-sample.txt")
        part2.solve(input).shouldBe(31)
    }

    @Test
    fun `part 2 input`() {
        val input = readInputText("day01-input.txt")
        part2.solve(input).shouldBe(0)
    }
}