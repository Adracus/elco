package de.adracus.elco.evaluator

/**
 * Created by axel on 27/06/15.
 */
class Scope[A, B](tuples: (A, B)*) {
  private var node = ScopeNode.root[A, B]()
  node ++= (tuples:_*)

  def apply(key: A) = node(key)

  def contains(key: A) = node.contains(key)

  def get(key: A) = node.get(key)

  def getOrElse(key: A, orElse: => B) = node.getOrElse(key, orElse)

  def ++=(tuples: (A, B)*): Unit = node ++= (tuples:_*)

  def ++=(map: Map[A, B]): Unit = node ++= map

  def +=(tuple: (A, B)) = node += tuple

  def push() = {
    node = node.makeChild()
    this
  }

  def set(key: A, value: B) = node.set(key, value)

  def pop(n: Int = 1) = {
    for (_ <- 0 until n) {
      node = node.parent.get
    }
    this
  }

  def condensed() = node.condensed()
}
