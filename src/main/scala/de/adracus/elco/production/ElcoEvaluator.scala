package de.adracus.elco.production

import de.adracus.elco.evaluator.{makeFunction, Evaluator, invoke}
import de.adracus.elco.grammar.Rule

import scala.collection.mutable

/**
 * Created by axel on 26/06/15.
 */
sealed trait Variable {
  def value: Any
  def value_=(value: Any): Unit
  def as[A]: A = value.asInstanceOf[A]
}

class Var(var value: Any) extends Variable
object Var {
  def apply(any: Any) = new Var(any)
}
case class Val(value: Any) extends Variable {
  def value_=(value: Any) = {
    throw new Exception("Cannot assign to val")
  }
}

class ElcoEvaluator extends Evaluator[String, Variable] {
  val Class = new Klass("Class", null, null)
  Class.klass = Class

  val Object = Class.extension("Object")
  val Method = Object.extension("Method")

  class MethodInstance(
    val fn: AnyVal)
    extends Instance(Method, Method.methods, Method.properties) {
    def call(args: List[Any]) = invoke(fn, args)
  }

  Class.sup = Object
  Object.sup = Object

  def mkMap(classes: Klass*) = {
    classes.foldLeft(Map[String, Val]()) { (acc, current) =>
      acc.+((current.name, Val(current)))
    }
  }

  scope ++= mkMap(Class, Object, Method)

  ('L := 'E & 'Separator & 'L) asLast()

  ('Separator := ";") unit()

  ('Separator := "NEWLINE") unit()

  ('ClassDef := "class" & "IDENTIFIER" & "{" & 'L & "}") eval {
    case Seq(_, name: String, _, l, _) =>
      println(s"DEFINE CLASS '$name'")
  }

  ('E := 'Call) asLast()

  ('Function := "fn" & "IDENTIFIER" & 'ArgList & "{" & 'L & "}") {
    case Seq(_, n, a, _, l, _) =>
      val name = e[String](n)
      val args = e[List[String]](a)
      val len = args.length
      val condensedScope = scope.condensed()

      val f = makeFunction(len) { values =>
        scope.push()
        scope ++= condensedScope
        for (i <- 0 until len) {
          scope += args(i) -> Val(values(i))
        }
        val res = e[Any](l)
        scope.pop()
        res
      }

      scope += name -> Val(new MethodInstance(f.asInstanceOf[AnyVal]))
  }

  ('ArgList := "(" & ")") constant List.empty[String]

  ('ArgList := "(" & 'List & ")") at 1

  ('List := "IDENTIFIER" & "," & 'List) eval {
    case Seq(name: String, _, list: List[_]) =>
      name +: list
  }

  ('List := "IDENTIFIER") eval (_.toList)

  ('L := 'E) asLast()

  ('Conditional := "if" & 'E & 'Wrapped & "else" & 'Wrapped) If 1 Then 2 Else 4

  ('Conditional := "if" & 'E & 'Wrapped) If 1 Then 2

  ('Wrapped := "{" & 'L & "}") at 1

  ('Wrapped := ":" & 'E) asLast()

  ('ExpList := 'E) eval(_.toList)

  ('ExpList := 'E & "," & 'ExpList) eval {
    case Seq(e, _, l: List[_]) =>
      e +: l
  }

  ('Call := "IDENTIFIER" & "(" & 'ExpList & ")") eval {
    case Seq(name: String, _, args: List[_], _) =>
      val method = scope(name).value
      if (!method.isInstanceOf[MethodInstance]) {
        throw new Exception("Cannot invoke non-function")
      }
      method.asInstanceOf[MethodInstance].call(args)
  }

  ('E := 'E & "COMPARE_OP" & 'E) eval {
    case Seq(a: Int, _, b: Int) => a == b
  }

  ('E := 'Function) asLast()

  ('E := "IDENTIFIER") eval {
    case Seq(name: String) => scope(name).value
  }

  ('E := "pass") unit()

  ('E := "INTEGER") asLast()

  ('E := 'E & "PLUS_OP" & 'E) eval {
    case Seq(e1: Int, _, e2: Int) => e1 + e2
  }

  ('E := 'E & "MINUS_OP" & 'E) eval {
    case Seq(e1: Int, _, e2: Int) => e1 - e2
  }

  ('E := 'Assignment) asLast()

  ('E := 'Conditional) asLast()

  ('Assignment := "IDENTIFIER" & "=" & 'E) eval {
    case Seq(name: String, _, e) =>
      val variable = scope.getOrElse(name, scope.set(name, new Var(null)))
      variable.value = e
      e
  }

  ('Assignment := "IDENTIFIER" & ":=" & 'E) eval {
    case Seq(name: String, _, e) =>
      if (scope contains name)
        throw new Exception("Cannot assign to already declared variable or value")
      scope.set(name, Val(e))
      e
  }

  override protected def defaultEvaluation(rule: Rule)(input: Seq[Any]): Any = {
    super.defaultEvaluation(rule)(input).asInstanceOf[List[_]].last
  }
}
