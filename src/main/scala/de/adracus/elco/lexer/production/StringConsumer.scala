package de.adracus.elco.lexer.production

import java.util.regex.Pattern

import de.adracus.elco.lexer.consumer.Consumer
import de.adracus.elco.lexer.core.{Hit, Unclosed, Match, Lexer}

/**
 * Created by axel on 21/05/15.
 */
class StringConsumer(val delimiterSymbol: String, val escapeSymbol: String, val newlineCharacter: String) extends Consumer {
  val delimiter = Pattern.quote(delimiterSymbol)
  val escape = Pattern.quote(escapeSymbol)

  def tryMatch(lexer: Lexer): Option[Match] = {
    if (lexer.pointsAt(delimiterSymbol)) {
      val remaining = lexer.remaining.substring(delimiterSymbol.length)
      val test = s"^[^$newlineCharacter]*(?<!$escape)$delimiter".r.findFirstMatchIn(remaining)
      if (test.isEmpty) {
        throw Unclosed("String", lexer.position)
      }
      val rawText = test.get.group(0).substring(0, test.get.end - delimiterSymbol.length)
      val text = rawText.replaceAll(Pattern.quote(escapeSymbol + delimiterSymbol), delimiterSymbol)
      Some(Hit(
        "STRING",
        delimiterSymbol.length * 2 + rawText.length,
        Some(text)))
    } else
      None
  }
}
