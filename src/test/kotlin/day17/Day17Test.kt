package day17

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import org.junit.jupiter.api.Test
import utils.readInputText

class Day17Test {
  private val part1 = Day17.Part1()
  private val part2 = Day17.Part2()

  @Test
  fun `part 1 example 1`() {
    // If register C contains 9, the program 2,6 would set register B to 1.
    val computer = Computer(registerC = Register(RegisterId.C, 9), programText = "2,6")
    val state = computer.run()
    state.registerAValue.shouldBe(0)
    state.registerBValue.shouldBe(1)
    state.registerCValue.shouldBe(9)
    state.output.shouldBeEmpty()
  }

  @Test
  fun `part 1 example 2`() {
    // If register A contains 10, the program 5,0,5,1,5,4 would output 0,1,2.
    val computer = Computer(registerA = Register(RegisterId.A, 10), programText = "5,0,5,1,5,4")
    val state = computer.run()
    state.registerAValue.shouldBe(10)
    state.registerBValue.shouldBe(0)
    state.registerCValue.shouldBe(0)
    state.output.shouldBe("0,1,2")
  }

  @Test
  fun `part 1 example 3`() {
    // If register A contains 2024, the program 0,1,5,4,3,0 would output 4,2,5,6,7,7,7,7,3,1,0 and
    // leave 0 in register A.
    val computer = Computer(registerA = Register(RegisterId.A, 2024), programText = "0,1,5,4,3,0")
    val state = computer.run()
    state.registerAValue.shouldBe(0)
    state.registerBValue.shouldBe(0)
    state.registerCValue.shouldBe(0)
    state.output.shouldBe("4,2,5,6,7,7,7,7,3,1,0")
  }

  @Test
  fun `part 1 example 4`() {
    // If register B contains 29, the program 1,7 would set register B to 26.
    val computer = Computer(registerB = Register(RegisterId.B, 29), programText = "1,7")
    val state = computer.run()
    state.registerAValue.shouldBe(0)
    state.registerBValue.shouldBe(26)
    state.registerCValue.shouldBe(0)
    state.output.shouldBeEmpty()
  }

  @Test
  fun `part 1 example 5`() {
    // If register B contains 2024 and register C contains 43690, the program 4,0 would set register
    // B to 44354.
    val computer =
      Computer(
        registerB = Register(RegisterId.B, 2024),
        registerC = Register(RegisterId.C, 43690),
        programText = "4,0",
      )
    val state = computer.run()
    state.registerAValue.shouldBe(0)
    state.registerBValue.shouldBe(44354)
    state.registerCValue.shouldBe(43690)
    state.output.shouldBeEmpty()
  }

  @Test
  fun `part 1 sample input`() {
    val input = readInputText("day17/day17-sample.txt")
    part1.solve(input).shouldBe("4,6,3,5,6,3,5,2,1,0")
  }

  @Test
  fun `part 1 input`() {
    val input = readInputText("day17/day17-input.txt")
    part1.solve(input).shouldBe("3,1,4,3,1,7,1,6,3")
  }

  @Test
  fun `part 2 sample input`() {
    val input =
      """
      Register A: 2024
      Register B: 0
      Register C: 0

      Program: 0,3,5,4,3,0
      """
        .trimIndent()
    part2.solve(input).shouldBe(117440)
  }

  @Test
  fun `part 2 input`() {
    val input = readInputText("day17/day17-input.txt")
    part2.solve(input).shouldBe(37221270076916L)
  }
}
