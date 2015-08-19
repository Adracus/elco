package de.adracus.elco.production.expressions

/**
 * Created by axel on 19/08/15.
 */
case class IdentifierList(identifiers: List[String]) extends Expression(identifiers:_*) {
  def :+(identifier: String) = IdentifierList(identifiers :+ identifier)
}

object IdentifierList {
  def empty() = IdentifierList(List.empty)
}
