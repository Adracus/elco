package de.adracus.elco.parser

import de.adracus.elco.lexer.core.{Token, TokenStream}
import de.adracus.elco.grammar.core.{Grammar, Word, End}

import scala.collection.mutable

/**
 * Created by axel on 15/06/15.
 */
class Parser(parseTable: ParseTable) {
  private val stack = new mutable.Stack[Int]()
  stack.push(0)

  def state = stack.top

  def action(token: Token) = {
    if (token.name == "EOF") parseTable.action(state, End)
    else {
      val terminal = Word(token.name)
      parseTable.action(state, terminal)
    }
  }

  def parse(tokenStream: TokenStream) = {
    var finished = false
    while (!finished && !tokenStream.isEmpty) {
      finished = step(tokenStream)
    }
    if (finished)
      println("Accepted")
    else
      println("Unexpected end of stream")
  }

  def step(tokenStream: TokenStream) = {
    val todo = action(tokenStream.lookahead)
    todo match {
      case Shift(next) =>
        stack.push(next)
        tokenStream.consume()
        false

      case Reduce(rule) =>
        println(rule.nonTerminal.name)
        for (_ <- 0 until rule.length)
          stack.pop()

        val next = parseTable.goto(state, rule.nonTerminal)
        stack.push(next)
        false

      case Accept =>
        true

    }
  }
}

object Parser {
  def parsing(grammar: Grammar) = {
    val parseTable = ParseTable.generate(grammar)
    new Parser(parseTable)
  }
}
