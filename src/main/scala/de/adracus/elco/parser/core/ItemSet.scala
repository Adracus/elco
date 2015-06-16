package de.adracus.elco.parser.core

import de.adracus.elco.grammar.core._

import scala.annotation.tailrec
import scala.collection.mutable

/**
 * Created by axel on 09/06/15.
 */
case class ItemSet(items: Set[Item]) extends Iterable[Item] {
  def advances(rules: Set[Rule]) = {
    pointingAt.map { producable =>
      (producable, advanceBy(producable, rules))
    }
  }

  def advanceBy(producable: Producable, rules: Set[Rule]) = {
    val nextItems = items.filter(_.pointsAt(producable))
    ItemSet.from(nextItems.map(_.advanced), rules)
  }

  def pointingAt = items.flatMap(_.after.headOption)

  def itemsAtStart = items.filter(_.isAtStart)

  override def iterator: Iterator[Item] = items.iterator
}

object ItemSet {
  def all(grammar: Grammar) = {
    val start = ItemSet.from(grammar.startRule, grammar.rules)
    val startSet = new mutable.LinkedHashSet[ItemSet]()
    startSet += start

    def recurse(acc: mutable.LinkedHashSet[ItemSet], next: Set[ItemSet]): List[ItemSet] = next.toList match {
      case head :: tail =>
        val newItems = head.advances(grammar.rules).map(_._2)
        val addition = newItems -- acc
        recurse(acc ++ addition, tail.toSet ++ addition)

      case Nil => acc.toList
    }

    recurse(startSet, startSet.toSet)
  }

  def from(rule: Rule, rules: Set[Rule]): ItemSet = from(Item.start(rule), rules)

  def from(item: Item, rules: Set[Rule]): ItemSet = from(Set(item), rules)

  def from(items: Set[Item], rules: Set[Rule]): ItemSet = {
    @tailrec
    def recurse(acc: Set[Item], done: Set[NonTerminal], next: List[NonTerminal]): ItemSet = next match {
      case nt :: tail =>
        val newItems = rules.filter(_.nonTerminal == nt).map(Item.start)
        val newNext = newItems.flatMap(_.headOption.collect {
          case nt: NonTerminal if !done.contains(nt) => nt
        })
        recurse(acc ++ newItems, done ++ newNext, tail ++ newNext)

      case Nil => ItemSet(acc)
    }

    recurse(items, Set.empty, items.map(_.pointerOption).collect {
      case Some(nt: NonTerminal) => nt
    }.toList)
  }
}
