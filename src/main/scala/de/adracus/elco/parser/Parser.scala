package de.adracus.elco.parser

import de.adracus.elco.grammar.core._
import de.adracus.elco.lexer.core.{TokenStream, Token}
import de.adracus.elco.lexer.production.ElcoLexer

import scala.collection.mutable

/**
 * Created by axel on 02/06/15.
 */
class Parser(val grammar: Grammar) {
  val actionTable = new ActionTableCalculator(grammar).computeActionTable()

  private val stack = new mutable.Stack[Int]()
  stack.push(0)

  def next(token: Token) = {
    val terminal = if ("EOF" == token.name) End else Terminal(token.name)
    actionTable((stack.top, terminal))
  }

  def goto(statement: Statement) = actionTable((stack.top, statement)).asInstanceOf[Goto]

  def parse(tokenStream: TokenStream): Unit = {
    while (tokenStream.hasNext) {
      val action = next(tokenStream.lookahead)
      action match {
        case Shift(state) =>
          tokenStream.consume()
          stack.push(state)
        case Reduce(rule) =>
          println(rule)
          for (i <- 0 until rule.length) stack.pop()
          val future = goto(rule.nonTerminal)
          stack.push(future.state)
        case Accept =>
          println("Accepted")
          return
      }
    }
  }
}

object Parser extends App {
  object MyGrammar extends Grammar {
    'S := 'L
    'L := 'E | 'E & 'L
    'E := 'E & "+" & 'E | "INTEGER"
  }

  val parser = new Parser(MyGrammar)
  val lexer = new ElcoLexer("1 + 2 + 3")
  val stream = new TokenStream(lexer)

  parser.parse(stream)
}
