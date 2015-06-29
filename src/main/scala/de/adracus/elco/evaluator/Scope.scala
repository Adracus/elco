package de.adracus.elco.evaluator

/**
 * Created by axel on 27/06/15.
 */
class Scope(tuples: (String, Any)*) {
  private var node = ScopeNode.root()
  node ++= (tuples:_*)

  def apply(key: String) = node(key)

  def ++=(tuples: (String, Any)*) = node ++= (tuples:_*)

  def +=(tuple: (String, Any)) = node += tuple

  def push() = node = node.makeChild()

  def set(key: String, value: Any) = node.set(key, value)

  def pop() = node = node.parent.get
}
