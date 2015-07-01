package de.adracus.elco.production

sealed trait BaseClass {
  val name: String
  val properties: Map[String, Property]
  val superClass: BaseClass

  def condensedProperties(): Map[String, Property] = {
    if (Base == this) this.properties
    else this.properties ++ superClass.condensedProperties()
  }
}

object Base extends BaseClass {
  val name = "Base"
  val superClass = Base

  val properties = Map.empty[String, Property]
}

class Class(
    val name: String,
    newProperties: Map[String, Property],
    val superClass: BaseClass = Base) extends BaseClass{
  val properties = superClass.condensedProperties() ++ newProperties
}
