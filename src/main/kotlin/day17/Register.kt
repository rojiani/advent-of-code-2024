package day17

class Register(val id: RegisterId, initialValue: Int) {
  private var value = initialValue

  val currentValue: Int
    get() = value

  /** Perform bitwise XOR of the register's value and the given operand. */
  fun xor(operand: Int): Int {
    value = value xor operand
    return value
  }

  internal fun setValue(newValue: Int): Int {
    value = newValue
    return value
  }

  override fun toString(): String = "Register${id.name}($value)"
}

enum class RegisterId {
  A,
  B,
  C,
}
