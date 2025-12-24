package day13

import java.math.BigInteger
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.math.roundToLong
import org.ojalgo.optimisation.ExpressionsBasedModel
import org.ojalgo.optimisation.Optimisation

/**
 * Day 13: Claw Contraption
 *
 * https://adventofcode.com/2024/day/13
 */
class Day13 {
  class Part1 {
    fun solve(input: String): Long {
      val clawMachines: List<ClawMachine> = parseInput(input)
      return clawMachines.sumOf { clawMachine -> minTokens(clawMachine) ?: 0L }
    }

    /**
     * Use [ojAlgo](https://www.ojalgo.org/) to solve this as a system of linear equations:
     * ```
     * Button A: X+94, Y+34
     * Button B: X+22, Y+67
     * Prize: X=8400, Y=5400
     * ```
     *
     * becomes:
     * ```
     * 94a + 22b = 8400
     * 34a + 67b = 5400
     * ```
     */
    private fun minTokens(clawMachine: ClawMachine): Long? {
      val (buttonA, buttonB, prize) = clawMachine
      val model = ExpressionsBasedModel()

      val a = model.addVariable("a").lower(0).integer().weight(buttonA.cost)
      val b = model.addVariable("b").lower(0).integer().weight(buttonB.cost)

      // Define the X Equation, e.g., 94a + 22b = 8400
      model.addExpression("X Equation").set(a, buttonA.x).set(b, buttonB.x).level(prize.x)

      // Define the Y Equation: 34a + 67b = 5400
      model.addExpression("Y Equation").set(a, buttonA.y).set(b, buttonB.y).level(prize.y)

      val result: Optimisation.Result = model.minimise()

      return when {
        result.state.isFeasible -> result.value.roundToLong()
        else -> null
      }
    }
  }

  class Part2 {
    fun solve(input: String): Long {
      val clawMachines: List<ClawMachine> = parseInput(input)
      val scaledClawMachines =
        clawMachines.map {
          it.copy(prize = Prize(it.prize.x + 10000000000000L, it.prize.y + 10000000000000L))
        }
      return scaledClawMachines.sumOf { clawMachine -> minTokens(clawMachine) ?: 0L }
    }

    /**
     * Use [Cramer's Rule](https://en.wikipedia.org/wiki/Cramer%27s_rule) since I'm not sure how to
     * modify the ojAlgo approach to handle BigIntegers properly.
     */
    private fun minTokens(clawMachine: ClawMachine): Long? {
      val (buttonA, buttonB, prize) = clawMachine

      // X equation: (aX)a + (bX)b = prizeX
      val aX = buttonA.x.toBigInteger()
      val bX = buttonB.x.toBigInteger()
      val prizeX = prize.x.toBigInteger()

      // Y equation: (aY)a + (bY)b = prizeY
      val aY = buttonA.y.toBigInteger()
      val bY = buttonB.y.toBigInteger()
      val prizeY = prize.y.toBigInteger()

      // 1. Calculate the main Determinant
      // Formula: (aX * bY) - (bX * aY)
      val determinant = (aX * bY) - (bX * aY)

      if (determinant == BigInteger.ZERO) {
        return null
      }

      // 2. Calculate Determinant for 'a'
      val determinantA = (prizeX * bY) - (bX * prizeY)

      // 3. Calculate Determinant for 'b'
      val determinantB = (aX * prizeY) - (prizeX * aY)

      // 4. Solve & make sure the solutions are ints
      val (a, remainderA) = determinantA.divideAndRemainder(determinant)
      val (b, remainderB) = determinantB.divideAndRemainder(determinant)
      if (remainderA != BigInteger.ZERO || remainderB != BigInteger.ZERO) {
        return null
      }

      return (a.toLong() * buttonA.cost) + (b.toLong() * buttonB.cost)
    }
  }
}

internal fun parseInput(input: String): List<ClawMachine> {
  return input.split(Regex("""\n\s*\n""")).map {
    val lines = it.lines().map { it.trim() }
    val (aLine, bLine, prizeLine) = lines

    val buttonA =
      Button.A(
        x = aLine.substringAfter("Button A: X+").substringBefore(",").toInt(),
        y = aLine.substringAfter("Y+").toInt(),
      )
    val buttonB =
      Button.B(
        x = bLine.substringAfter("Button B: X+").substringBefore(",").toInt(),
        y = bLine.substringAfter("Y+").toInt(),
      )
    val prize =
      Prize(
        x = prizeLine.substringAfter("X=").substringBefore(',').toLong(),
        y = prizeLine.substringAfter("Y=").toLong(),
      )

    ClawMachine(buttonA, buttonB, prize)
  }
}

data class ClawMachine(val a: Button.A, val b: Button.B, val prize: Prize)

sealed class Button {
  abstract val x: Int
  abstract val y: Int
  abstract val cost: Int

  data class A(override val x: Int, override val y: Int) : Button() {
    override val cost: Int
      get() = 3
  }

  data class B(override val x: Int, override val y: Int) : Button() {
    override val cost: Int
      get() = 1
  }
}

data class Prize(val x: Long, val y: Long)
