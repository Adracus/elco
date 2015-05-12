package lexer

/**
 * Created by axel on 11/05/15.
 */
class Ignorer(val regex: String) extends Consumer {
  override def consume(lexingState: LexingState): Option[Token] = {
    lexingState.stepRegex(regex)
    None
  }
}
