package de.adracus.elco.base

/**
 * Created by axel on 15/07/15.
 */
trait BaseClass[+A <: BaseInstance] extends BaseInstance {
  val name: String
  val properties: Map[String, BaseInstance]
  def create(arguments: Any*): A
  val clazz: BaseClass[_ <: BaseInstance]
  val superClazz: BaseClass[_ <: BaseInstance]
}
