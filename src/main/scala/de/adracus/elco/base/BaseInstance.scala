package de.adracus.elco.base

/**
 * Created by axel on 15/07/15.
 */
trait BaseInstance {
  val properties: Map[String, BaseInstance]
  val clazz: BaseClass[_ <: BaseInstance]
}

case class Instance(clazz: BaseClass[_ <: BaseInstance], properties: Map[String, BaseInstance])
  extends BaseInstance