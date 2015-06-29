package de.adracus.elco.evaluator

import scala.collection.mutable

/**
 * Created by axel on 27/06/15.
 */
class ScopeNode(val parent: Option[ScopeNode]) {
  private val values = new mutable.HashMap[String, Any]()

  def apply(key: String): Any = {
    values.getOrElse(key, {
      parent.map(_.apply(key)).getOrElse({
        throw new LookupException(key)
      })
    })
  }

  def ++=(tuples: (String, Any)*): Unit = {
    tuples.foreach {
      case (name, value) => +=(name, value)
    }
  }

  def +=(tuple: (String, Any)): Any = {
    set(tuple._1, tuple._2)
  }

  def setAll(tuples: Seq[(String, Any)]) = values ++= tuples

  def isRoot = parent.isDefined

  def condensed(): Map[String, Any] = {
    parent.map(_.condensed())
      .getOrElse(Map.empty[String, Any]) ++ values
  }

  def set(key: String, value: Any) = {
    values(key) = value
    value
  }

  def makeChild() = new ScopeNode(Some(this))

  class LookupException(name: String) extends Exception(s"Could not find '$name'")
}

object ScopeNode {
  def root() = new ScopeNode(None)
}
