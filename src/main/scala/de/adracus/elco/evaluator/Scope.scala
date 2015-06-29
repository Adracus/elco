package de.adracus.elco.evaluator

/**
 * Created by axel on 27/06/15.
 */
class Scope(tuples: (String, Any)*) {
  private var node = ScopeNode.root()
  node ++= (tuples:_*)

  def apply(key: String) = node(key)

  def ++=(tuples: (String, Any)*): Unit = node ++= (tuples:_*)

  def ++=(map: Map[String, Any]): Unit = node ++= map

  def +=(tuple: (String, Any)) = node += tuple

  def push() = {
    node = node.makeChild()
    this
  }

  def set(key: String, value: Any) = node.set(key, value)

  def pop(n: Int = 1) = {
    for (_ <- 0 until n) {
      node = node.parent.get
    }
    this
  }

  def condensed() = node.condensed()
}
