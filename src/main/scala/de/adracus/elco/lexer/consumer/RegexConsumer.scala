package de.adracus.elco.lexer.consumer

import java.util.regex.Pattern

import de.adracus.elco.lexer.core.{Hit, Match, Lexer}

/**
 * Created by axel on 20/05/15.
 */
class RegexConsumer(regex: String, val name: String, val transform: Option[String => Any] = None) extends Consumer {
  val r = regex.r

  def tryMatch(lexer: Lexer): Option[Match] = {
    val possibleMatch = lexer startMatch r
    if (possibleMatch.isDefined) {
      val matched = possibleMatch.get
      if (transform.isDefined) {
        Some(Hit(name, matched.length, Some(transform.get(matched))))
      } else {
        Some(Hit(name, matched.length, None))
      }
    } else {
      None
    }
  }
}

object RegexConsumer {
  def literal(literal: String) = new RegexConsumer(
    Pattern.quote(literal), literal.toUpperCase)
}