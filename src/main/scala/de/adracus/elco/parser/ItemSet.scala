package de.adracus.elco.parser

import de.adracus.elco.grammar.core.{Statement, Rule, NonTerminal}

sealed trait BaseItemSet
/**
 * Created by axel on 03/06/15.
 */
case class ItemSet(items: Set[Item]) extends Iterable[Item] with BaseItemSet {
  def canAdvance = items.forall(!_.isAtEnd)

  def advanceSymbols = items.flatMap(_.next)

  def advance(rules: Map[NonTerminal, Set[Rule]]) = {
    advanceSymbols.map(advanceBy(_, rules))
  }

  def advanceBy(statement: Statement, rules: Map[NonTerminal, Set[Rule]]) = {
    val filtered = items.filter(_.pointsAt(statement)).map(_.advance())
    ItemSet.buildSet(filtered, rules)
  }

  override def iterator: Iterator[Item] = items.iterator

  override def toString() = items.mkString("\n")
}

object FinalSet extends BaseItemSet

object ItemSet {
  def buildSet(start: Set[Item], rules: Map[NonTerminal, Set[Rule]]) =  {
    def inner(checked: Set[Item], current: Set[Item]): Set[Item] = current.toList match {
      case Nil => checked
      case head :: tail => head.pointsAtNonTerminal match {
        case true =>
          val matching = rules(head.nextAsNonTerminal.get).map(Item.start)
          inner(checked + head, tail.toSet ++ (matching -- checked))
        case false =>
          inner(checked + head, tail.toSet)
      }
    }

    ItemSet(inner(Set.empty, start))
  }

  def empty = ItemSet(Set.empty)
}
