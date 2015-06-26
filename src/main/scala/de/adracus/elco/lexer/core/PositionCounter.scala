package de.adracus.elco.lexer.core

/**
 * Created by axel on 20/05/15.
 */
class PositionCounter(val newlineSymbol: String) {
  private var column = 0
  private var line = 0
  private var ct = 0

  def current = Position(column, line)

  def step(string: String): Unit = {
    var i = 0
    while (i < string.length) {
      if (string.substring(i) == newlineSymbol) {
        column = 0
        line += 1
        i += newlineSymbol.length
      } else {
        column += 1
      }
      i += 1
      ct += 1
    }
  }

  def value = ct
}
