package de.adracus.elco.base

/**
 * Created by axel on 15/07/15.
 */
trait BaseClass[+A <: BaseInstance] extends BaseInstance {
  val name: String
  val properties: Map[String, _ <: BaseInstance]
  def create(arguments: Any*): A
  val clazz: BaseClass[_ <: BaseInstance]
  val superClazz: BaseClass[_ <: BaseInstance]

  def boundMethods(instance: BaseInstance): Map[String, BaseInstance] = {
    properties.mapValues {
      case m: MethodInstance => m.curried(instance)
    }
  }
}
