package lexer

import util.IterableUtil.RichIterable

/**
 * Created by axel on 08/05/15.
 */
class LexingState(txt: String) {
  private val columnCounter = new Counter()
  private val lineCounter = new Counter()
  private val text = new LexingText(txt)

  def pointsAt(chars: String) = text.pointsAt(chars)
  def regexPointsAt(regex: String) = text.regexPointsAt(regex)

  def stepLine(lines: Int = 1, lineWidth: Int = 1): Unit ={
    text.step(lines * lineWidth)
    lineCounter.step(lines)
    columnCounter.reset()
  }

  def stepColumn(columns: Int = 1): Unit = {
    text.step(columns)
    columnCounter.step(columns)
  }

  def stepRegex(regex: String): Option[String] = {
    val matched = text.matchRegex(regex)
    if (matched.isDefined) {
      stepColumn(matched.get.length)
    }
    matched
  }

  def matchRegex(regex: String) = text.matchRegex(regex)

  def stepUntilEscaped(
                 end: Set[String],
                 escapes: Set[String] = Set("\\"),
                 failing: Set[String] = Set.empty[String],
                 lineSeparator: String = "\n"): String = {
    var result = ""
    var escaped = false
    while (escaped || !(end.forall(pointsAt(_)))) {
      failing.foreach((element: String) => {
        if (pointsAt(element)) throw new UnallowedSymbol(element, position)
      })
      escaped = false
      val escapedCandidate = escapes.firstWhere(pointsAt(_))
      if (escapedCandidate.isDefined) {
        escaped = true
        stepColumn(escapedCandidate.get.length)
      } else {
        result += current
        if (pointsAt(lineSeparator)) {
          stepLine(1, lineSeparator.length)
        } else {
          stepColumn()
        }
      }
    }
    result
  }

  def stepUntil(
                 end: Set[String],
                 failing: Set[String] = Set.empty[String],
                 lineSeparator: String = "\n"): String = {
    stepUntilEscaped(end, Set.empty[String], failing, lineSeparator)
  }


  def current = text.current

  def position = new Position(columnCounter.current, lineCounter.current)

  def makeToken(identifier: String) = Some(Token(identifier, position, None))
  def makeToken(identifier: String, value: Any) = Some(Token(identifier, position, Some(value)))

  def charsLeft = text.charsLeft
  def noCharsLeft = !charsLeft
}

class UnallowedSymbol(val symbol: String, val position: Position) extends Exception {
  override def toString: String = s"Unallowed symbol $symbol at $position"
}

case class Token (val identifier: String, val position: Position, val value: Option[Any]) {
  override def toString: String = if (value.isDefined) s"<$identifier, ${value.get}>" else s"<$identifier>"
}
