package de.adracus.elco.evaluate

import scala.collection.mutable

/**
 * Created by axel on 15/07/15.
 */
class Stack[A] private (initial: StorageMap[A]) {
  def this() = this(new StorageMap[A])

  private val stack = new mutable.Stack[StorageMap[A]]()
  stack.push(initial)

  def push() = stack.push(new StorageMap[A])

  def pop() = stack.pop()

  def condensed = {
    val initial = stack.reverseIterator.reduce {(acc, current) =>
      acc ++ current
    }
    new Stack[A](initial)
  }

  def onGetFailed(key: A) = None

  def get(key: A): Option[Any] = {
    def recurse(iterator: Iterator[StorageMap[A]]): Option[Storage] = {
      if (iterator.hasNext) {
        val current = iterator.next()
        val option = current get key
        if (option.isDefined) option
        else recurse(iterator)
      } else
        onGetFailed(key)
    }

    recurse(stack.iterator).map(_.value)
  }

  private def set(key: A, value: Any, onNotFound: => Any) = {
    def recurse(iterator: Iterator[StorageMap[A]]): Any = {
      if (iterator.hasNext) {
        val current = iterator.next()
        val option = current.get(key)
        if (option.isDefined) {
          option.get.value = value
        } else {
          recurse(iterator)
        }
      } else onNotFound
    }

    recurse(stack.iterator)
  }

  def mut(key: A, value: Any) = set(key, value, stack.top.mut(key, value))

  def const(key: A, value: Any) = set(key, value, stack.top.const(key, value))
}

class LookupException[A](key: A) extends Exception(s"Could not find key '$key'")
