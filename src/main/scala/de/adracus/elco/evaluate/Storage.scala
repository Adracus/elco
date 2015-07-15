package de.adracus.elco.evaluate

/**
 * Created by axel on 15/07/15.
 */
sealed trait Storage {
  def isConst: Boolean
  def isMutable: Boolean = !isConst
  def value: Any
  def value_=(value: Any): Any
}

case class Val(value: Any) extends Storage {
  def isConst = true

  def value_=(value: Any) = throw new IllegalStateException("Cannot set constant")
}

case class Var(private var _value: Any) extends Storage {
  def isConst = false

  def get = value

  def value = _value

  def value_=(value: Any) = {
    _value = value
    _value
  }
}
