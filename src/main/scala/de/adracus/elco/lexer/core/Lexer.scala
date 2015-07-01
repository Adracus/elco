package de.adracus.elco.lexer.core

import de.adracus.elco.lexer.consumer.{Consumer, RegexConsumer, RegexIgnorer}

import scala.collection.mutable.ListBuffer

abstract class Lexer {
  def newLineSymbol: String = "\n"

  private val consumers = new ListBuffer[Consumer]

  def ignore(regex: String) = addConsumer(new RegexIgnorer(regex))

  def symbol(symbol: String) = keyword(symbol)

  def regex(regex: String, name: String): Unit = {
    addConsumer(new RegexConsumer(regex, name))
  }

  def regex(regex: String, name: String, conversion: String => Any): Unit = {
    addConsumer(new RegexConsumer(regex, name, Some(conversion)))
  }

  def keyword(keyword: String): Unit = addConsumer(RegexConsumer.literal(keyword))

  def tryMatch(lexingText: LexingText) = {
    consumers.flatMap(_.tryMatch(lexingText))
  }

  def lex(string: String) = new TokenStream(string, this)

  def addConsumer(consumer: Consumer) = consumers append consumer
}
