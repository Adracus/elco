package de.adracus.elco.parser

import de.adracus.elco.grammar.core._

import scala.annotation.tailrec
import scala.collection.mutable

/**
 * Created by axel on 09/06/15.
 */
case class ItemSet(items: Set[Item]) {
  def advances(rules: Set[Rule]) = {
    pointingAt.map { producable =>
      val nextItems = items.filter(_.pointsAt(producable))
      (producable, ItemSet.from(nextItems.map(_.advanced), rules))
    }
  }

  def pointingAt = items.flatMap(_.after.headOption)
}

object ItemSet {
  def all(grammar: Grammar) = {
    @tailrec
    def recurse(acc: mutable.LinkedHashSet[ItemSet], next: List[ItemSet]): List[ItemSet] = next match {
      case head :: tail =>
        val advances = head.advances(grammar.rules).map(_._2)
        recurse(acc ++= advances, tail ++ (advances -- acc))
      case Nil => acc.toList
    }

    val start = ItemSet.from(grammar.startRule, grammar.rules)
    val startSet = new mutable.LinkedHashSet[ItemSet]()
    startSet += start
    recurse(startSet, List(start))
  }

  def from(rule: Rule, rules: Set[Rule]): ItemSet = from(Item.start(rule), rules)

  def from(item: Item, rules: Set[Rule]): ItemSet = from(Set(item), rules)

  def from(items: Set[Item], rules: Set[Rule]): ItemSet = {
    @tailrec
    def recurse(acc: Set[Item], done: Set[NonTerminal], next: List[Item]): ItemSet = next match {
      case head :: tail =>
        val afterOption = head.after.headOption
        if (afterOption.isEmpty) recurse(acc, done, tail)
        else {
          val after = afterOption.get
          after match {
            case t: Terminal => recurse(acc, done, tail)
            case nt: NonTerminal =>
              if (done contains nt) recurse(acc, done, tail)
              else {
                val newItems = rules.filter(_.nonTerminal == nt).map(Item.start)
                recurse(acc ++ newItems, done + nt, tail ++ newItems)
              }
          }
        }

      case Nil => ItemSet(acc)
    }

    recurse(items, Set.empty, items.toList)
  }
}
