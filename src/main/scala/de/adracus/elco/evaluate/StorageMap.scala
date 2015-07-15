package de.adracus.elco.evaluate

import scala.collection.mutable

/**
 * Created by axel on 15/07/15.
 */
class StorageMap[A] private (initial: mutable.HashMap[A, Storage]) extends mutable.Map[A, Storage] {
  def this() = this(new mutable.HashMap[A, Storage]())

  private val _values = new mutable.HashMap[A, Storage]()

  override def +=(kv: (A, Storage)): StorageMap.this.type = {
    _values += kv
    this
  }

  override def -=(key: A): StorageMap.this.type = {
    _values -= key
    this
  }

  def ++(other: StorageMap[A]): StorageMap[A] = {
    val newValues = (new mutable.HashMap[A, Storage]() ++= _values) ++= other._values
    new StorageMap(newValues)
  }

  def const(key: A, value: Any) = {
    if (_values.contains(key))
      throw new IllegalStateException("Cannot assign to already defined variable")
    _values(key) = Val(value)
    this
  }

  def mut(key: A, value: Any) = {
    val option = _values.get(key)
    val mapped = option.map(_.value = value)
    if (mapped.isEmpty) {
      _values(key) = Var(value)
    }
    this
  }

  def toMap = _values.mapValues(_.value)

  override def get(key: A): Option[Storage] = _values.get(key)

  override def iterator: Iterator[(A, Storage)] = _values.iterator
}

object StorageMap {
  def empty[A] = new StorageMap[A]
}
