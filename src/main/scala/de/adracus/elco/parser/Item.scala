package de.adracus.elco.parser

import de.adracus.elco.grammar.core.{NonTerminal, Rule, Statement}

/**
 * Created by axel on 02/06/15.
 */
case class Item(marker: Int, rule: Rule) {
  def before = rule.dropRight(rule.length - marker)

  def after = rule.drop(marker)

  def isAtStart = 0 == marker

  def isAtEnd = marker == rule.length

  def pointsAt(statement: Statement) = if (isAtEnd) false else next.get == statement

  def pointsAtNonTerminal = if (after.isEmpty) false else after.head.isInstanceOf[NonTerminal]

  def next = after.headOption

  def nextAsNonTerminal = after.headOption.map(_.asInstanceOf[NonTerminal])

  def advance() = {
    assert(!isAtEnd)
    Item(marker + 1, rule)
  }

  override def toString = rule.nonTerminal.name + " := " + before.mkString(" ") + " . " + after.mkString(" ")
}

object Item {
  def start(rule: Rule) = Item(0, rule)
}
