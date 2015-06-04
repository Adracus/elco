package de.adracus.elco.parser

/**
 * Created by axel on 04/06/15.
 */
sealed trait Action

sealed trait Transition extends Action {
  val target: ItemSet
}

case class Shift(target: ItemSet)

case class Goto(target: ItemSet)

object Accept extends Action
