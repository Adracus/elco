package de.adracus.elco.parser

import de.adracus.elco.grammar.core._
import de.adracus.elco.lexer.core.Token

import scala.collection.mutable

/**
 * Created by axel on 02/06/15.
 */
class Parser(val grammar: Grammar) {
  val actionTable = new ActionTableCalculator(grammar).computeActionTable()
}
