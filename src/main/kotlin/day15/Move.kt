package day15

enum class Move(val symbol: Char) {
  UP('^'),
  DOWN('v'),
  LEFT('<'),
  RIGHT('>');

  val isVertical: Boolean
    get() = this == UP || this == DOWN

  val isHorizontal: Boolean
    get() = this == LEFT || this == RIGHT

  companion object {
    fun fromSymbol(symbol: Char): Move = Move.entries.first { it.symbol == symbol }
  }
}
