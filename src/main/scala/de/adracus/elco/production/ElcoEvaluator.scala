package de.adracus.elco.production

import de.adracus.elco.evaluator.{makeFunction, invoke, Evaluator}
import de.adracus.elco.grammar.Rule

/**
 * Created by axel on 26/06/15.
 */
class ElcoEvaluator extends Evaluator {
  ('L := 'E & ";" & 'L) eval {
    case Seq(_, _, e) => e
  }

  ('ClassDef := "class" & "IDENTIFIER" & "{" & 'L & "}") eval {
    case Seq(_, name: String, _, l, _) =>
      println(s"DEFINE CLASS '$name'")
  }

  ('E := 'Call) eval {
    case Seq(call) => call
  }

  ('Function := "fn" & "IDENTIFIER" & 'ArgList & "{" & 'L & "}") {
    case Seq(_, n, a, _, l, _) =>
      import de.adracus.elco.evaluator.wrap._

      val name = e[String](n)
      val args = e[List[String]](a)
      println(s"DEFINE FUNCTION $name WITH ARGS ${args.mkString(", ")}")
      val len = args.length

      val condensedScope = scope.condensed()
      def prolog() = {
        scope.push()
        scope ++= condensedScope
      }
      val f = makeFunction(len, () =>
        e[Any](l)).wrapped({ vals =>
        prolog()
        scope.push()
        for (i <- 0 until len) {
          scope += (args(i), vals(i))
        }
      }, (_, _) => scope.pop(2))

      scope += name -> f
      f
  }

  ('ArgList := "(" & ")") eval {
    _ => List.empty[String]
  }

  ('ArgList := "(" & 'List & ")") eval {
    case Seq(_, list, _) => list
  }

  ('List := "IDENTIFIER" & "," & 'List) eval {
    case Seq(name: String, _, list: List[_]) =>
      name +: list
  }

  ('List := "IDENTIFIER") eval {
    case Seq(name: String) => List(name)
  }

  ('L := 'E) eval {
    case Seq(e) => e
  }

  ('Conditional := "if" & 'E & "{" & 'L & "}" & "else" & "{" & 'L & "}") {
    case Seq(_, condition, _, ifBody, _, _, _, elseBody, _) =>
      if (e[Boolean](condition)) {
        e[Any](ifBody)
      } else {
        e[Any](elseBody)
      }
  }

  ('ExpList := 'E) eval {
    case Seq(e) => List(e)
  }

  ('ExpList := 'E & "," & 'ExpList) eval {
    case Seq(e, _, l: List[_]) =>
      e +: l
  }

  ('Call := "IDENTIFIER" & "(" & 'ExpList & ")") eval {
    case Seq(name: String, _, args: List[_], _) =>
      import de.adracus.elco.evaluator.invoke._
      println("Invoking " + name)
      scope(name)(args)
  }

  ('E := 'E & "==" & 'E) eval {
    case Seq(a: Int, _, b: Int) => a == b
  }

  ('E := 'Function) eval {
    case Seq(e) => e
  }

  ('E := "IDENTIFIER") eval {
    case Seq(name: String) => scope(name)
  }

  ('E := "pass") eval {
    _ => Unit
  }

  ('E := "INTEGER") eval {
    case Seq(i) => i
  }

  ('E := 'E & "+" & 'E) eval {
    case Seq(e1: Int, _, e2: Int) => e1 + e2
  }

  ('E := 'E & "-" & 'E) eval {
    case Seq(e1: Int, _, e2: Int) => e1 - e2
  }

  ('E := 'Conditional) eval {
    case Seq(c) => c
  }

  ('E := "IDENTIFIER" & "=" & 'E) eval {
    case Seq(name: String, _, e) =>
      scope += name -> e
  }

  override protected def defaultEvaluation(rule: Rule)(input: Seq[Any]): Any = {
    super.defaultEvaluation(rule)(input).asInstanceOf[List[_]].last
  }
}
