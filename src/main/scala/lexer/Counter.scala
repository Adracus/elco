package lexer

/**
 * Created by axel on 08/05/15.
 */
class Counter(start: Int = 0) {
  private var _ct: Int = start

  def step(increment: Int = 1): Unit = {
    if (increment < 0) throw new IllegalArgumentException("Increment has to be >= 0")
    _ct += increment
  }

  def skipTo(newCount: Int): Unit = {
    if (_ct <= newCount) _ct = newCount
    else throw new IllegalArgumentException("newCount has to be larger than current")
  }

  def reset() = _ct = 0

  def current: Int = _ct
}
