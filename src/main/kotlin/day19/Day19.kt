package day19

import kotlin.collections.plus

class Day19 {

  class Part1 {
    fun solve(input: String): Int {
      val lines = input.lines()
      val availablePatterns: Set<String> = lines.first().split(", ").toSet()

      val desiredPatterns = lines.drop(2).toSet()

      val cache = mutableMapOf<String, List<String>>()
      return desiredPatterns.count { pattern ->
        val stripeCombinations = makePattern(pattern, availablePatterns, cache)
        stripeCombinations.isNotEmpty()
      }
    }

    private fun makePattern(
      pattern: String,
      availablePatterns: Set<String>,
      cache: MutableMap<String, List<String>>,
    ): List<String> {
      makePattern(
        remainingPattern = pattern,
        availablePatterns = availablePatterns,
        patternsUsed = emptyList(),
        cache = cache,
      )
      return cache.getOrDefault(pattern, emptyList())
    }

    private fun makePattern(
      remainingPattern: String,
      availablePatterns: Set<String>,
      patternsUsed: List<String>,
      cache: MutableMap<String, List<String>>,
    ): List<String> =
      when {
        remainingPattern in cache -> cache.getValue(remainingPattern)
        remainingPattern.isEmpty() -> {
          val pattern = patternsUsed.joinToString(separator = "")
          cache[pattern] = patternsUsed
          return patternsUsed
        }
        else -> {
          for (availablePattern in availablePatterns) {
            if (remainingPattern.startsWith(availablePattern)) {
              val patternsForRemaining =
                makePattern(
                  remainingPattern = remainingPattern.drop(availablePattern.length),
                  availablePatterns = availablePatterns,
                  patternsUsed = patternsUsed + availablePattern,
                  cache = cache,
                )
              if (patternsForRemaining.isNotEmpty()) return patternsForRemaining
            }
          }
          emptyList()
        }
      }
  }

  class Part2 {
    fun solve(input: String): Int {
      TODO()
    }
  }
}
