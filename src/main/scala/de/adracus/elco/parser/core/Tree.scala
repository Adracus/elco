package de.adracus.elco.parser.core

import de.adracus.elco.grammar.core.Rule
import de.adracus.elco.lexer.core.Token

/**
 * Created by axel on 15/06/15.
 */
sealed trait Tree {
  def name: String
}

case class Leaf(token: Token) extends Tree {
  def name = token.name
}

case class Node(rule: Rule, children: Seq[Tree]) extends Tree {
  def name = rule.nonTerminal.name
}


