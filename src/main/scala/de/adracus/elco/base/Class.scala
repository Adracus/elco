package de.adracus.elco.base

import de.adracus.elco.evaluate.Stack

case class UserClass(
    name: String,
    properties: Map[String, Instance],
    clazz: BaseClass[_ <: BaseInstance],
    superClazz: BaseClass[_ <: BaseInstance])
  extends BaseClass[Instance] {

  def create(arguments: Any*) = Instance(this, Map())
}

object Class extends BaseClass[BaseClass[_]] {
  val name = "Class"
  val clazz = Class
  val superClazz = Object
  val properties = Map[String, Instance]()

  def create(args: Any*) = args match {
    case Seq(name: String, properties: Map[String, Instance]) =>
      UserClass(name, properties, Class, Object)
  }

  def subClass(clazz: BaseClass[_ <: BaseInstance]) = {
    UserClass
  }
}

object Method extends BaseClass[Instance] {
  override val name: String = "Method"

  override def create(arguments: Any*): Instance = {
    throw new IllegalStateException("Cannot call create on method")
  }

  def createMethod[A](stack: Stack[A], body: () => Any, argNames: List[A]) = {
    def pushAll(arguments: List[Any]): Unit = {
      def recurse(restNames: List[A], restArgs: List[Any]): Unit = (restNames, restArgs) match {
        case (Nil, Nil) => ;

        case ((argName :: names, arg :: args)) =>
          stack.const(argName, arg)
          recurse(names, args)
      }

      recurse(argNames, arguments)
    }

    (args: List[Any]) => {
      stack.push()
      pushAll(args)
      val res = body()
      stack.pop()
      res
    }
  }

  override val properties: Map[String, BaseInstance] = Map.empty
  override val superClazz: BaseClass[_ <: BaseInstance] = Object
  override val clazz: BaseClass[_ <: BaseInstance] = Class
}
