package de.adracus.elco.lexer.core

import de.adracus.elco.lexer.consumer.{Consumer, RegexConsumer, RegexIgnorer}

import scala.collection.mutable.ListBuffer

abstract class Lexer {
  def newLineSymbol: String = "\n"

  private val consumers = new ListBuffer[Consumer]

  def ignore(regex: String) = addConsumer(new RegexIgnorer(regex))

  def symbol(symbol: String) = keyword(symbol)

  def keyword(keyword: String): Unit = addConsumer(RegexConsumer.literal(keyword))

  def tryMatch(lexingText: LexingText) = {
    consumers.flatMap(_.tryMatch(lexingText))
  }

  def lex(string: String) = new TokenStream(string, this)

  def addConsumer(consumer: Consumer) = consumers append consumer
}
