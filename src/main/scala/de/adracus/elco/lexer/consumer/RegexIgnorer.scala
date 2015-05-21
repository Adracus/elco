package de.adracus.elco.lexer.consumer

import de.adracus.elco.lexer.core.{Empty, Match, Lexer}

/**
 * Created by axel on 20/05/15.
 */
class RegexIgnorer(regex: String) extends Consumer {
  val r = regex.r

  def tryMatch(lexer: Lexer): Option[Match] = {
    val matchTry = lexer startMatch r
    if (matchTry.isDefined) {
      Some(Empty(matchTry.get.length))
    } else {
      None
    }
  }
}
