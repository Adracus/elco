package de.adracus.elco.evaluator

import scala.collection.mutable

/**
 * Created by axel on 27/06/15.
 */
class ScopeNode[A, B](val parent: Option[ScopeNode[A, B]]) {
  private val values = new mutable.HashMap[A, B]()

  def apply(key: A): B = {
    values.getOrElse(key, {
      parent.map(_.apply(key)).getOrElse({
        throw new LookupException(key)
      })
    })
  }

  def getOrElse(key: A, orElse: => B): B = {
    values.getOrElse(key, parent.map(_.getOrElse(key, orElse)).getOrElse(orElse))
  }

  def get(key: A): Option[B] = {
    val value = values.get(key)
    if (value.isDefined) value
    else {
      parent.map(_.get(key)).getOrElse(None)
    }
  }

  def ++=(tuples: (A, B)*): Unit = {
    tuples.foreach {
      case (name, value) => +=(name, value)
    }
  }

  def ++=(map: Map[A, B]): Unit = {
    map.foreach(this += _)
  }

  def +=(tuple: (A, B)): B = {
    set(tuple._1, tuple._2)
    tuple._2
  }

  def setAll(tuples: Seq[(A, B)]) = values ++= tuples

  def isRoot = parent.isDefined

  def condensed(): Map[A, B] = {
    parent.map(_.condensed())
      .getOrElse(Map.empty[A, B]) ++ values
  }

  def set(key: A, value: B) = {
    values(key) = value
    value
  }

  def makeChild() = new ScopeNode(Some(this))

  def remove(key: A): Option[B] = {
    val removed = values.remove(key)
    if (removed.isEmpty)
      parent.map(_.remove(key)).getOrElse(None)
    else
      removed
  }

  def contains(key: A): Boolean = {
    val res = values.contains(key)
    if (res)
      true
    else
      parent.exists(_.contains(key))
  }

  class LookupException(name: A) extends Exception(s"Could not find '$name'")
}

object ScopeNode {
  def root[A, B]() = new ScopeNode[A, B](None)
}
