package com.rojiani.day01

import kotlin.math.abs

class Day01 {

    class Part1 {
        fun solve(input: String): Int {
            val (left, right) = parseInput(input)
            val sortedLeft = left.sorted()
            val sortedRight = right.sorted()

            var totalDistance = 0
            sortedLeft.zip(sortedRight).forEach { (left, right) ->
                totalDistance += abs(left - right)
            }

            return totalDistance
        }
    }
}

internal fun parseInput(input: String): Pair<List<Int>, List<Int>> {
    val lines = input.lines().map { line -> line.trim() }
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()
    for (line in lines) {
        val numbers = line.split("\\s".toRegex()).map { it.toInt() }
        check(numbers.size == 2) { "Line $line has more than two numbers" }
        left.add(numbers[0])
        right.add(numbers[1])
    }
    check(left.size == right.size)
    return Pair(left, right)
}

