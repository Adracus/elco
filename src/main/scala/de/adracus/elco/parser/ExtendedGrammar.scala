package de.adracus.elco.parser

import de.adracus.elco.grammar.core.{Statement, Grammar}

import scala.collection.mutable

/**
 * Created by axel on 04/06/15.
 */
class ExtendedGrammar(val rules: Set[ExtendedRule]) {
  def toGrammar() = {
    val result = new Grammar()
    rules.map(_.toRule).foreach(result.add)
    result
  }

  override def toString() = rules.mkString("\n")
}
