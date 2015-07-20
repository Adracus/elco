package de.adracus.elco.base

import de.adracus.elco.evaluate.Stack

case class UserClass(
    name: String,
    properties: Map[String, _ <: BaseInstance],
    clazz: BaseClass[_ <: BaseInstance],
    superClazz: BaseClass[_ <: BaseInstance])
  extends BaseClass[BaseInstance] {

  def create(arguments: Any*) = new Instance(this, boundMethods)
}

class IntegerInstance(val value: Int, props: BaseInstance => Map[String, BaseInstance])
  extends Instance(Integer.asInstanceOf[BaseClass[_ <: BaseInstance]], props) {
}

object IntegerInstance {
  def unapply(inst: IntegerInstance) = Some((inst.value, inst.properties))
}

object Integer extends BaseClass[IntegerInstance] {
  override val properties = Map(
    "plus" -> new MethodInstance({
      case List(IntegerInstance(self, _), IntegerInstance(other, _)) =>
        val total = self + other
        new IntegerInstance(total, boundMethods)
    })
  )

  override val clazz = Class

  override val superClazz = Object

  override val name: String = "Integer"

  override def create(arguments: Any*) = arguments match {
    case Seq(i: Int) => new IntegerInstance(i, boundMethods)
  }
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

case class MethodInstance(method: List[Any] => Any) extends BaseInstance {
  override val properties: Map[String, BaseInstance] = Map("call" -> this)

  override val clazz: BaseClass[_ <: BaseInstance] = Method

  def curried(args: Any*) = MethodInstance((list: List[Any]) => method((args ++ list).toList))
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

    MethodInstance((args: List[Any]) => {
      stack.push()
      pushAll(args)
      val res = body()
      stack.pop()
      res
    })
  }

  override val properties: Map[String, BaseInstance] = Map.empty
  override val superClazz: BaseClass[_ <: BaseInstance] = Object
  override val clazz: BaseClass[_ <: BaseInstance] = Class
}
