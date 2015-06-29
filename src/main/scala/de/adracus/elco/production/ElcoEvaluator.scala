package de.adracus.elco.production

import de.adracus.elco.evaluator.Evaluator
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

  ('Function := "fn" & "IDENTIFIER" & 'ArgList & "{" & 'L & "}") eval {
    case Seq(_, name: String, args: List[_], _, l, _) =>
      println(s"DEFINE FUNCTION $name WITH ARGS ${args.mkString(", ")}")
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

  ('E := "IDENTIFIER" & "=" & 'E) eval {
    case Seq(name: String, _, e) =>
      scope.set(name, e)
  }

  override protected def defaultEvaluation(rule: Rule)(input: Seq[Any]): Any = {
    super.defaultEvaluation(rule)(input).asInstanceOf[List[_]].last
  }
}
