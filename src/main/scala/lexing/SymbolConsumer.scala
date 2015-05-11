package lexing

/**
 * Created by axel on 11/05/15.
 */
class SymbolConsumer(val symbol: String) extends Consumer {
  override def consume(lexingState: LexingState): Option[Token] = {
    if (lexingState.pointsAt(symbol)) {
      lexingState.stepColumn(symbol.length)
      lexingState.makeToken(symbol)
    } else None
  }
}
