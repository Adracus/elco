package de.adracus.elco.lexer.consumer

import de.adracus.elco.lexer.core.{LexingText, Match}

/**
 * Created by axel on 20/05/15.
 */
trait Consumer {
  def tryMatch(lexingText: LexingText): Option[Match]
}
