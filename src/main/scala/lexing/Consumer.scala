package lexing

/**
 * Created by axel on 08/05/15.
 */
trait Consumer {
  def consume(lexingState: LexingState): Option[Token]
}