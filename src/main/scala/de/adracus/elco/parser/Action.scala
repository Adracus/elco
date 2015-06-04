package de.adracus.elco.parser

import de.adracus.elco.grammar.core.Rule

/**
 * Created by axel on 04/06/15.
 */
sealed trait Action

case class Reduce(rule: Rule)

sealed trait Transition extends Action {
  val state: Int
}

case class Shift(state: Int)

case class Goto(state: Int)

object Accept extends Action {
  override def toString() = "Accept"
}
