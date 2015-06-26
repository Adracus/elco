package de.adracus.elco.production

import de.adracus.elco.evaluator.Evaluator
import de.adracus.elco.grammar.ProductionDSL

import scala.collection.mutable

/**
 * Created by axel on 26/06/15.
 */
class ElcoEvaluator extends Evaluator with ProductionDSL {
  val values = new mutable.HashMap[String, Any]()

  ('L := 'E & ";" & 'L) {
    case Seq(_, _, e) => e
  }

  ('L := 'E) {
    case Seq(e) => e
  }

  ('E := "IDENTIFIER") {
    case Seq(name: String) => values(name)
  }

  ('E := "INTEGER") {
    case Seq(i) => i
  }

  ('E := 'E & "+" & 'E) {
    case Seq(e1: Int, _, e2: Int) => e1 + e2
  }

  ('E := "IDENTIFIER" & "=" & 'E) {
    case Seq(name: String, _, e) =>
      values(name) = e
      e
  }
}
