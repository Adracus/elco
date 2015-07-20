package de.adracus.elco.base

/**
 * Created by axel on 15/07/15.
 */
trait BaseInstance {
  val properties: Map[String, BaseInstance]
  val clazz: BaseClass[_ <: BaseInstance]

  def get(name: String) = properties.get(name)

  def apply(name: String) = properties(name)
}

class Instance(val clazz: BaseClass[_ <: BaseInstance], props: BaseInstance => Map[String, BaseInstance])
  extends BaseInstance {

  lazy val properties = props(this)
}

object Instance {
  def apply(clazz: BaseClass[_ <: BaseInstance], props: BaseInstance => Map[String, BaseInstance]) =
    new Instance(clazz, props)

  def apply(clazz: BaseClass[_ <: BaseInstance], props: => Map[String, BaseInstance]) =
    new Instance(clazz, (_) => props)

  def unapply(instance: Instance) = Some((instance.clazz, instance.properties))
}