package de.adracus.elco.production

import de.adracus.elco.evaluator.{makeFunction, Evaluator}
import de.adracus.elco.grammar.Rule

/**
 * Created by axel on 26/06/15.
 */
class ElcoEvaluator extends Evaluator {
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
      import de.adracus.elco.evaluator.wrap._

      val name = e[String](n)
      val args = e[List[String]](a)
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

  ('ArgList := "(" & ")") constant List.empty[String]

  ('ArgList := "(" & 'List & ")") at 1

  ('List := "IDENTIFIER" & "," & 'List) eval {
    case Seq(name: String, _, list: List[_]) =>
      name +: list
  }

  ('List := "IDENTIFIER") eval (_.toList)

  ('L := 'E) asLast()

  ('Conditional := "if" & 'E & 'Wrapped & "else" & 'Wrapped) If 1 Then 2 Else 4

  ('Wrapped := "{" & 'L & "}") at 1

  ('Wrapped := 'E) asLast()

  ('ExpList := 'E) eval(_.toList)

  ('ExpList := 'E & "," & 'ExpList) eval {
    case Seq(e, _, l: List[_]) =>
      e +: l
  }

  ('Call := "IDENTIFIER" & "(" & 'ExpList & ")") eval {
    case Seq(name: String, _, args: List[_], _) =>
      import de.adracus.elco.evaluator.invoke._
      val res = scope(name)(args)
      res
  }

  ('E := 'E & "COMPARE_OP" & 'E) eval {
    case Seq(a: Int, _, b: Int) => a == b
  }

  ('E := 'Function) asLast()

  ('E := "IDENTIFIER") eval {
    case Seq(name: String) => scope(name)
  }

  ('E := "pass") unit()

  ('E := "INTEGER") asLast()

  ('E := 'E & "PLUS_OP" & 'E) eval {
    case Seq(e1: Int, _, e2: Int) => e1 + e2
  }

  ('E := 'E & "MINUS_OP" & 'E) eval {
    case Seq(e1: Int, _, e2: Int) => e1 - e2
  }

  ('E := 'Conditional) asLast()

  ('E := "IDENTIFIER" & "=" & 'E) eval {
    case Seq(name: String, _, e) =>
      scope += name -> e
  }

  override protected def defaultEvaluation(rule: Rule)(input: Seq[Any]): Any = {
    super.defaultEvaluation(rule)(input).asInstanceOf[List[_]].last
  }
}
