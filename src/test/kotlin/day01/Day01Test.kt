package day01

import com.rojiani.day01.Day01
import com.rojiani.utils.readInputText

import io.kotest.matchers.shouldBe

import org.junit.jupiter.api.Test

class Day01Test {
//    private val day01 = Day01()
    private val day01Part1 = Day01.Part1()

    @Test
    fun `part 1 sample input`() {
        val input = readInputText("day01-sample.txt")
        println(input)


        day01Part1.solve(input) shouldBe

    }

}