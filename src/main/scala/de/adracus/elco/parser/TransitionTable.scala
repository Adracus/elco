package de.adracus.elco.parser

import de.adracus.elco.grammar.core.{Rule, Producable}

import scala.annotation.tailrec

/**
 * Created by axel on 10/06/15.
 */
class TransitionTable(itemSets: List[ItemSet], rules: Set[Rule]) {
  require(itemSets.toSet.size == itemSets.size, "Item Sets has to be duplicate free and in order")

  @tailrec
  private def recurse(acc: Map[(ItemSet, Producable), ItemSet], next: List[ItemSet]): Map[(ItemSet, Producable), ItemSet] = next match {
    case head :: tail =>
      val advances = head.advances(rules).map {
        case (statement, future) => ((head, statement), future)
      }
      recurse(acc ++ advances, tail)

    case Nil => acc
  }

  val transitions = recurse(Map.empty, itemSets)
}
