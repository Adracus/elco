package lexer

/**
 * Created by axel on 08/05/15.
 */
class StringConsumer(
                      val delimiter: String = "\"",
                      val escapeSymbol: String = "\\",
                      val failsOn: Set[String] = Set("\n"),
                      val lineDelimiter: String = "\n") extends Consumer {
  override def consume(lexingState: LexingState): Option[Token] = {
    val start = lexingState.position
    if (lexingState pointsAt delimiter) {
      lexingState.stepColumn(delimiter.length)
      try {
        val word = lexingState.stepUntilEscaped(Set(delimiter), Set(escapeSymbol), failsOn, lineDelimiter)
        lexingState.stepColumn(delimiter.length)
        lexingState.makeToken("STRING", word)
      } catch {
        case be: IndexOutOfBoundsException => throw new UnclosedException("String", start)
      }
    } else {
      None
    }
  }
}