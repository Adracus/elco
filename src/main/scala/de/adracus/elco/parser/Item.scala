package de.adracus.elco.parser

import de.adracus.elco.grammar.core.{Producable, Rule}

/**
 * Created by axel on 09/06/15.
 */
case class Item(marker: Int, rule: Rule) extends Iterable[Producable] {
  require(marker >= 0 && marker <= rule.length, message = "Marker out of bounds")

  override def iterator: Iterator[Producable] = rule.iterator

  def before = rule.drop(rule.length - marker)

  def after = rule.dropRight(marker)

  def pointsAt(producable: Producable) = {
    val afterOption = after.headOption
    if (afterOption.isEmpty) false
    else afterOption.get == producable
  }

  def advanced = Item(marker + 1, rule)

  def nonTerminal = rule.nonTerminal

  override def toString(): String =
    s"$nonTerminal := " + before.mkString(" ") + " . " + after.mkString(" ")
}

object Item {
  def start(rule: Rule) = Item(0, rule)
}
