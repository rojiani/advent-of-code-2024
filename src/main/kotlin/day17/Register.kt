package day17

class Register(val id: RegisterId, initialValue: Long) {
  private var value = initialValue

  val currentValue: Long
    get() = value

  /** Perform bitwise XOR of the register's value and the given operand. */
  fun xor(operand: Long): Long {
    value = value xor operand
    return value
  }

  internal fun setValue(newValue: Long): Long {
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
