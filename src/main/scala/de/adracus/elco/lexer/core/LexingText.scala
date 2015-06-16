package de.adracus.elco.lexer.core

import scala.util.matching.Regex

/**
 * Created by axel on 20/05/15.
 */
class LexingText(val text: String, val newlineSymbol: String = "\n") {
  private val counter = new PositionCounter(newlineSymbol)

  def pointsAt(string: String): Boolean = remaining.startsWith(string)

  def startMatch(regex: Regex) = s"^${regex.pattern}".r.findFirstIn(remaining)

  def current = text(counter.value).toString

  def remaining = text.substring(counter.value)

  def step(length: Int): Unit = {
    counter.step(until(length))
  }

  def position = counter.current

  def until(length: Int) = remaining.substring(0, length)

  def hasNext = counter.value < text.length
}
