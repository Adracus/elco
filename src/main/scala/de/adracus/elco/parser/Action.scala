package de.adracus.elco.parser

import de.adracus.elco.grammar.Rule

/**
 * Created by axel on 14/06/15.
 */
sealed trait Action

object Accept extends Action

sealed case class ShiftReduce(shift: Shift, reduce: Reduce) extends Action

sealed case class Reduce(rule: Rule) extends Action

sealed trait Transition extends Action {
  val state: Int
}

sealed case class Shift(state: Int) extends Transition