package lexer

import util.IterableUtil.RichIterable
/**
 * Created by axel on 11/05/15.
 */
class WhitespaceConsumer(val blanks: Set[String] = Set(" ", "\t"), val newLineSymbol: String = "\n") extends Consumer {
  override def consume(lexingState: LexingState): Option[Token] = {
    while (lexingState.charsLeft) {
      val blankMatch = blanks.firstWhere(lexingState.pointsAt(_))
      if (blankMatch.isDefined) {
        lexingState.stepColumn(blankMatch.get.length)
      } else if (lexingState pointsAt newLineSymbol) {
        lexingState.stepLine(lineWidth = newLineSymbol.length)
      } else {
        return None
      }
    }
    None
  }
}
