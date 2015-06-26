package de.adracus.elco.lexer.core

import scala.collection.mutable.ListBuffer

/**
 * Created by axel on 30/05/15.
 */
class TokenStream(val string: String, lexer: Lexer) extends Iterator[Token] {
  private val text = new LexingText(string, lexer.newLineSymbol)

  private var finished = false

  private val _current: ListBuffer[Token] = new ListBuffer[Token]

  def lookahead: Token = lookahead()

  def lookahead(n: Int = 0): Token = {
    while (_current.length < n + 1) {
      _current += next()
    }
    _current(n)
  }

  final def next(): Token = {
    if (_current.nonEmpty) {
      val result = _current.head
      _current.trimStart(1)
      return result
    }

    if (finished) throw new IllegalStateException("Already at end of text")

    if (!text.hasNext) {
      finished = true
      return Token("EOF", text.position)
    }

    val matches = lexer.tryMatch(text)
    if (matches.isEmpty)
      throw new UnrecognizedSymbol(text.current, text.position)

    val longestMatch = matches.maxBy(_.length)
    longestMatch match {
      case Hit(name, length, value) =>
        val start = position
        text.step(length)
        new Token(name, start, value)

      case Empty(length) =>
        text.step(length)
        next()
    }
  }

  def position = text.position

  def hasNext = !finished

  override def isEmpty = _current.isEmpty && !hasNext
}
