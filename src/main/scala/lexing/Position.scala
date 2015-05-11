package lexing

/**
 * Created by axel on 08/05/15.
 */
case class Position(column: Int, line: Int) extends Ordered[Position] {
  override def compare(that: Position): Int = {
    val lineCompare = this.line compareTo that.line
    if (0 == lineCompare) this.column compareTo that.line else lineCompare
  }

  def add(column: Int, line: Int) = new Position(this.column + column, this.line + line)

  override def toString: String = s"Column $column Line $line"
}
