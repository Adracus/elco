package lexing

/**
 * Created by axel on 08/05/15.
 */
class LexingText(val text: String, start: Int = 0) {
  private val counter = new Counter(start)

  def pointsAt(chars: String) = text.startsWith(chars, counter.current)
  def regexPointsAt(regex: String) = matchRegex(regex).isDefined

  def step(length: Int = 1): Unit = {
    if (counter.current + length > text.length) {
      throw new IndexOutOfBoundsException()
    }
    counter.step(length)
  }

  def matchRegex(regex: String) = s"^$regex".r.findFirstIn(text.substring(counter.current))

  def charsLeft = counter.current < text.length

  def current = text.charAt(counter.current).toString
}
