package de.adracus.elco.lexer.core

/**
 * Created by axel on 30/05/15.
 */
class TokenStream(val lexer: Lexer) {
  private var _current: Seq[Token] = Seq.empty

  def consume() = _current = _current.drop(1)

  def lookahead: Token = lookahead()

  def lookahead(n: Int = 0): Token = {
    while (_current.length < n + 1) {
      _current = _current :+ lexer.next()
    }
    _current(n)
  }

  def hasNext: Boolean = lexer.hasNext
}
