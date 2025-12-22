package day11

import kotlin.collections.getOrPut
import kotlin.text.split

/**
 * Day 11: Plutonian Pebbles
 *
 * https://adventofcode.com/2024/day/11
 */
class Day11 {

  class Part1 {
    fun solve(input: String, blinks: Int): Int {
      val stones = parseInput(input)
      val afterBlinks = blinkStones(stones, blinks)
      return afterBlinks.size
    }

    private fun blinkStones(initialStones: List<Stone>, blinks: Int): List<Stone> {
      var stones = initialStones
      repeat(blinks) { stones = blinkStonesOnce(stones) }
      return stones
    }

    private fun blinkStonesOnce(stones: List<Stone>): List<Stone> {
      val stonesAfter = mutableListOf<Stone>()
      for (stone in stones) {
        stonesAfter.addAll(stone.blink())
      }
      return stonesAfter.toList()
    }

    private fun parseInput(input: String): List<Stone> =
      input.lines().single().split(' ').map { Stone(it) }

    data class Stone(val digits: String) {

      init {
        check(digits.length == 1 || !digits.startsWith("0")) {
          "Digits should not have leading zeroes, but was: $digits"
        }
      }

      fun blink(): List<Stone> =
        when {
          digits == "0" -> listOf(Stone("1"))
          digits.length % 2 == 0 ->
            listOf(
              Stone(digits.take(digits.length / 2)),
              Stone(digits.takeLast(digits.length / 2).toLong().toString()),
            )
          else -> listOf(Stone((digits.toLong() * 2024).toString()))
        }
    }
  }

  class Part2 {
    fun solve(input: String, blinks: Int = 75): Long {
      val stones = input.lines().single().split(' ').map { Stone(it) }
      val cache = mutableMapOf<State, Long>()
      return stones.sumOf { stone -> numberOfStones(stone, times = blinks, cache) }
    }

    private fun numberOfStones(stone: Stone, times: Int, cache: MutableMap<State, Long>): Long {
      val state = State(stone, times)
      return when {
        times == 0 -> 1L
        state in cache -> cache.getValue(state)
        stone.digits == "0" ->
          cache.getOrPut(state) {
            numberOfStones(stone = Stone(digits = "1"), times = times - 1, cache = cache)
          }
        stone.digits.length % 2 == 0 ->
          cache.getOrPut(state) {
            val firstHalfState =
              State(
                stone = Stone(digits = stone.digits.take(stone.digits.length / 2)),
                remainingBlinks = times - 1,
              )
            val firstHalf =
              cache.getOrPut(firstHalfState) {
                numberOfStones(
                  stone = firstHalfState.stone,
                  times = firstHalfState.remainingBlinks,
                  cache = cache,
                )
              }
            val secondHalfState =
              State(
                stone = Stone(stone.digits.takeLast(stone.digits.length / 2).toLong().toString()),
                remainingBlinks = times - 1,
              )
            val secondHalf =
              cache.getOrPut(secondHalfState) {
                numberOfStones(
                  stone = secondHalfState.stone,
                  times = secondHalfState.remainingBlinks,
                  cache = cache,
                )
              }
            firstHalf + secondHalf
          }
        else ->
          cache.getOrPut(state) {
            numberOfStones(
              stone = Stone((stone.digits.toLong() * 2024).toString()),
              times = times - 1,
              cache = cache,
            )
          }
      }
    }

    data class State(val stone: Stone, val remainingBlinks: Int)

    data class Stone(val digits: String)
  }
}
