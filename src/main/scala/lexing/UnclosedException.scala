package lexing

/**
 * Created by axel on 08/05/15.
 */
class UnclosedException(val name: String, val start: Position) extends Exception {
  override def toString: String = s"Unclosed $name at $start"
}
