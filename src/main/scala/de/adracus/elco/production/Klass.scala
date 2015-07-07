package de.adracus.elco.production

import de.adracus.elco.evaluator.{invoke, isFunction}

import scala.collection.mutable

trait Instantiatable {
  var klass: Klass
  val methods: mutable.HashMap[String, Instantiatable]
  val properties: mutable.HashMap[String, Instantiatable]

  def isA(that: Klass) = {
    this.klass.isAssignable(that)
  }
}

class Instance(
    var klass: Klass,
    val methods: mutable.HashMap[String, Instantiatable],
    val properties: mutable.HashMap[String, Instantiatable]) extends Instantiatable {

}

class Klass(val name: String, var sup: Klass, var klass: Klass) extends Instantiatable {
  val methods: mutable.HashMap[String, Instantiatable] = mutable.HashMap.empty
  val properties: mutable.HashMap[String, Instantiatable] = mutable.HashMap.empty

  class Subclass(name: String, sup: Klass) extends Klass(name, sup, this)

  def isAssignable(klass: Klass): Boolean = {
    if (this == klass) true
    else if (null == sup) false
    else sup.isAssignable(klass)
  }

  def extension(name: String): Klass = {
    new Subclass(name, this)
  }

  def create(args: Seq[Any]): Instantiatable = {
    val instance = new Instance(this, methods, properties)
    instance
  }
}

object Bootstrap extends App {
  def apply() = {
    val Class = new Klass("Class", null, null)
    Class.klass = Class

    val Object = Class.extension("Object")
    val Method = Object.extension("Method")

    class MethodInstance(
        val fn: AnyVal,
        methods: mutable.HashMap[String, Instantiatable],
        properties: mutable.HashMap[String, Instantiatable])
          extends Instance(Method, methods, properties) {
    }

    Class.sup = Object
    Object.sup = Object

    def mkMap(classes: Klass*) = {
      classes.foldLeft(Map[String, Klass]()){ (acc, current) =>
        acc.+((current.name, current))
      }
    }

    mkMap(Class, Object, Method)
  }

  apply()
}