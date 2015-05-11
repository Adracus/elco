package lexing

import scala.collection.mutable.ListBuffer

/**
 * Created by axel on 08/05/15.
 */
class Lexer {
  private val _consumers: ListBuffer[Consumer] = new ListBuffer[Consumer]

  def append(consumers: Consumer*) = _consumers.append(consumers:_*)

  def lex(text: String) = new LexingIterator(_consumers.toList, text)
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
