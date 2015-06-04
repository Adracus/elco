package de.adracus.elco.lexer.core

import de.adracus.elco.lexer.consumer.{Consumer, RegexConsumer, RegexIgnorer}

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

class Lexer(string: String, newlineSymbol: String = "\n") extends Iterator[Token] {
  private val consumers = new ListBuffer[Consumer]
  private var finished = false

  private val text = new LexingText(string, newlineSymbol)

  def current = text.current

  def pointsAt(string: String) = text pointsAt string

  def ignore(regex: String) = addConsumer(new RegexIgnorer(regex))

  def symbol(symbol: String) = keyword(symbol)

  def keyword(keyword: String): Unit = addConsumer(RegexConsumer.literal(keyword))

  def startMatch(regex: Regex) = text startMatch regex

  def remaining = text.remaining

  private def step(length: Int) = text.step(length)

  def addConsumer(consumer: Consumer) = consumers append consumer

  final def next(): Token = {
    if (finished) throw new IllegalStateException("Already at end of text")

    if (!text.hasNext) {
      finished = true
      return Token("EOF", text.position)
    }

    val matches = consumers.flatMap(_.tryMatch(this))
    if (matches.isEmpty)
      throw new UnrecognizedSymbol(text.current, text.position)

    val longestMatch = matches.maxBy(_.length)
    longestMatch match {
      case Hit(name, length, value) =>
        val start = position
        step(length)
        new Token(name, start, value)

      case Empty(length) =>
        step(length)
        next()
    }
  }

  def position = text.position

  def hasNext = !finished
}
