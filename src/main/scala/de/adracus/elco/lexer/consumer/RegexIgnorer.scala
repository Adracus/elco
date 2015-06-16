package de.adracus.elco.lexer.consumer

import de.adracus.elco.lexer.core.{Empty, LexingText, Match}

/**
 * Created by axel on 20/05/15.
 */
class RegexIgnorer(regex: String) extends Consumer {
  val r = regex.r

  def tryMatch(text: LexingText): Option[Match] = {
    val matchTry = text startMatch r
    if (matchTry.isDefined) {
      Some(Empty(matchTry.get.length))
    } else {
      None
    }
  }
}
