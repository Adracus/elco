package lexer

/**
 * Created by axel on 08/05/15.
 */
class CommentConsumer(
                    val startSymbol: String = "/*",
                    val endSymbol: String = "*/",
                    val newLineSymbol: String= "\n") extends Consumer{

  override def consume(lexingState: LexingState): Option[Token] = {
    val start = lexingState.position
    if (lexingState pointsAt startSymbol) {
      try {
        lexingState.stepUntil(Set(endSymbol), lineSeparator = newLineSymbol)
        lexingState.stepColumn(endSymbol.length) // Step over the end symbol
      } catch {
        case be: IndexOutOfBoundsException => throw new UnclosedException("Comment", start)
      }
    }
    None
  }
}