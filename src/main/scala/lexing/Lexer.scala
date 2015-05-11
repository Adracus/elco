package lexing

import scala.collection.mutable.ListBuffer

/**
 * Created by axel on 08/05/15.
 */
class Lexer {
  private val _consumers: ListBuffer[Consumer] = new ListBuffer[Consumer]

  def append(consumers: Consumer*) = _consumers.append(consumers:_*)

  protected def on = new LexingBehaviour(this)

  def lex(text: String) = new LexingIterator(_consumers.toList, text)

  protected class LexingBehaviour(lexer: Lexer) {
    protected class RegexLexingBehaviour(val regex: String) {
      def makeTokenWithIdentifier(identifier: String) = lexer.append(new RegexConsumer(identifier, regex))
      def ignore() = lexer.append(new Ignorer(regex))
    }

    protected class SymbolLexingBehaviour(val symbol: String) {
      def consume() = lexer.append(new SymbolConsumer(symbol))
    }

    def symbol(symbol: String) = new SymbolLexingBehaviour(symbol)
    def whitespace(whitespaceConsumer: WhitespaceConsumer) = lexer.append(new WhitespaceConsumer())
    def regex(regex: String) = new RegexLexingBehaviour(regex)
    def strings(stringConsumer: StringConsumer) = lexer.append(new StringConsumer())
  }
}

class LexingIterator(val consumers: List[Consumer], text: String) extends Iterator[Token] {
  private val state = new LexingState(text)
  private var endWasShown = false

  def hasNext = !endWasShown

  def next(): Token = {
    while (state.charsLeft) {
      for (consumer <- consumers) {
        val consumed = consumer.consume(state)
        if (consumed.isDefined) {
          return consumed.get
        }
      }
      throw new UnknownCharacterException(state.current, state.position)
    }
    endWasShown = true
    state.makeToken("EOF").get
  }
}

class UnknownCharacterException(char: String, position: Position) extends Exception {
  override def toString: String = s"Unknown character: '$char' at $position"
}
