package day05

import com.google.common.graph.GraphBuilder
import com.google.common.graph.ImmutableGraph

typealias PageNumber = Int

/**
 * Day 5: Print Queue
 *
 * https://adventofcode.com/2024/day/5
 */
class Day05 {

  class Part1 {
    fun solve(input: String): Int {
      val (orderingRules, pages) = parseInput(input)
      val graph = buildGraph(orderingRules)

      val correctlyOrderedUpdates: List<List<PageNumber>> =
        pages.filter { update -> isInCorrectOrder(graph, updatePages = update) }

      return correctlyOrderedUpdates.sumOf { update -> update[update.size / 2] }
    }
  }

  class Part2 {
    fun solve(input: String): Int {
      val (orderingRules, pages) = parseInput(input)
      val graph = buildGraph(orderingRules)

      val incorrectlyOrderedUpdates: List<List<PageNumber>> =
        pages.filter { update -> !isInCorrectOrder(graph, updatePages = update) }

      val fixedUpdates: List<List<PageNumber>> =
        incorrectlyOrderedUpdates.map { fixOrdering(graph, it) }

      return fixedUpdates.sumOf { update -> update[update.size / 2] }
    }

    private fun fixOrdering(
      graph: ImmutableGraph<PageNumber>,
      incorrectOrdering: List<PageNumber>,
    ): List<PageNumber> = topologicalSort(graph, incorrectOrdering.toSet())
  }
}

internal fun parseInput(text: String): OrderingRulesAndPages {
  val lines = text.lines()
  val orderingRules: List<OrderingRule> =
    lines
      .takeWhile { "|" in it }
      .map { line ->
        OrderingRule(
          before = line.substringBefore("|").toInt(),
          after = line.substringAfter("|").toInt(),
        )
      }

  val updatePages: List<List<PageNumber>> =
    lines.drop(orderingRules.size + 1).map { line ->
      line.split(",").filter { it.isNotEmpty() }.map { it.trim().toInt() }
    }

  return OrderingRulesAndPages(orderingRules, updatePages)
}

data class OrderingRulesAndPages(
  val orderingRules: List<OrderingRule>,
  val updatePages: List<List<PageNumber>>,
)

data class OrderingRule(val before: PageNumber, val after: PageNumber)

private fun buildGraph(orderingRules: List<OrderingRule>): ImmutableGraph<PageNumber> =
  GraphBuilder.directed()
    .allowsSelfLoops(false)
    .immutable<PageNumber>()
    .apply {
      for (orderingRule in orderingRules) {
        val (before, after) = orderingRule
        addNode(before)
        addNode(after)
        putEdge(before, after)
      }
    }
    .build()

private fun isInCorrectOrder(
  graph: ImmutableGraph<PageNumber>,
  updatePages: List<PageNumber>,
): Boolean {
  val pairs = updatePages.zipWithNext()
  return pairs.all { (a, b) -> b in graph.successors(a) }
}

fun topologicalSort(
  graph: ImmutableGraph<PageNumber>,
  nodesToInclude: Set<PageNumber>,
): List<PageNumber> {
  val visited = mutableSetOf<PageNumber>()
  val currentPath = mutableSetOf<PageNumber>()
  val results = ArrayDeque<PageNumber>()

  for (node in nodesToInclude) {
    if (node !in visited) {
      if (!dfsSort(node, nodesToInclude, graph, visited, currentPath, results)) {
        throw IllegalArgumentException(
          "Graph contains a cycle; topological sort not possible. node: $node, path: $currentPath, results: $results"
        )
      }
    }
  }

  return results.reversed()
}

private fun dfsSort(
  node: PageNumber,
  nodesToInclude: Set<PageNumber>,
  graph: ImmutableGraph<PageNumber>,
  visited: MutableSet<PageNumber>,
  currentPath: MutableSet<PageNumber>,
  results: ArrayDeque<PageNumber>,
): Boolean {
  visited += node
  currentPath += node

  // Visit all neighbors
  for (neighbor in graph.successors(node).filter { it in nodesToInclude }) {
    if (neighbor in currentPath) {
      // Cycle detected: neighbor is already in the current DFS path
      return false
    }
    if (neighbor !in visited) {
      if (!dfsSort(neighbor, nodesToInclude, graph, visited, currentPath, results)) {
        return false
      }
    }
  }

  currentPath.remove(node)
  // Add node to stack after all its dependencies (neighbors) are processed
  results.addLast(node)
  return true
}
