package de.adracus.elco.lexer.consumer

import de.adracus.elco.lexer.core.{Lexer, Match}

/**
 * Created by axel on 20/05/15.
 */
trait Consumer {
  def tryMatch(lexer: Lexer): Option[Match]
}
