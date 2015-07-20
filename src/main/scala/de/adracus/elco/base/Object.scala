package de.adracus.elco.base

/**
 * Created by axel on 15/07/15.
 */
object Object extends BaseClass[Instance] {
  override val name = "Object"
  override val properties: Map[String, BaseInstance] = Map.empty

  override def create(arguments: Any*): Instance = {
    throw new Exception("Cannot create instance of object")
  }

  override val superClazz: BaseClass[_ <: BaseInstance] = Object
  override val clazz: BaseClass[_ <: BaseInstance] = Class
}
