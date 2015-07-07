package de.adracus.elco.parser

import de.adracus.elco.grammar._
import de.adracus.elco.lexer.core.{Token, TokenStream}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * Created by axel on 15/06/15.
 */
class Parser(parseTable: ParseTable) {
  private val stack = new mutable.Stack[Int]()
  private val treeStack = new mutable.Stack[Tree]()
  stack.push(0)

  def state = stack.top

  def action(token: Token) = parseTable.action(state, wrap(token))

  def wrap(token: Token) = if (token.name == "EOF") End else Word(token.name)

  def parse(tokenStream: TokenStream) = {
    var finished = false
    while (!finished && !tokenStream.isEmpty) {
      finished = step(tokenStream)
    }
    stack.clear()
    stack.push(0)
    if (finished) {
      val result = treeStack.pop()
      reset()
      result.asInstanceOf[Node]
    } else {
      reset()
      throw UnexpectedException(
        tokenStream.lookahead,
        parseTable.expected(state))
    }
  }

  private def reduce(rule: Rule) = {
    val buffer = new ListBuffer[Tree]()
    for (_ <- 0 until rule.length) {
      stack.pop()
      buffer.append(treeStack.pop())
    }

    treeStack.push(Node(rule, buffer.reverse.toSeq))
  }

  def reset() = {
    treeStack.clear()
    stack.clear()
    stack.push(0)
  }

  def execute(tokenStream: TokenStream, action: Action): Boolean = action match {
    case Shift(next) =>
      stack.push(next)
      val leaf = Leaf(tokenStream.next())
      treeStack.push(leaf)
      false

    case Reduce(rule) =>
      reduce(rule)

      val next = parseTable.goto(state, rule.nonTerminal)
      stack.push(next)
      false

    case ShiftReduce(Shift(state), Reduce(rule)) =>
      val proceed = execute(tokenStream, _: Action)

      def correctAction(precedence: Precedence) = {
        if (precedence.pType == Left) Reduce(rule) else Shift(state)
      }

      val lookahead = tokenStream.lookahead.name
      if (rule.length <= 1) {
        val shiftPrecedence = parseTable.precedenceOf(lookahead)
        return proceed(if (shiftPrecedence.isEmpty) Shift(state) else correctAction(shiftPrecedence.get))
      }
      val reduceSymbol = rule.toSeq(1).name

      val reducePrecedence = parseTable.precedenceOf(reduceSymbol)
      val shiftPrecedence = parseTable.precedenceOf(lookahead)

      if (shiftPrecedence.isEmpty && reducePrecedence.isEmpty)
        proceed(Shift(state))
      else if (shiftPrecedence.isEmpty && reducePrecedence.isDefined)
        proceed(correctAction(reducePrecedence.get))
      else if (reducePrecedence.isEmpty && shiftPrecedence.isDefined)
        proceed(correctAction(shiftPrecedence.get))
      else if (reducePrecedence.get > shiftPrecedence.get)
        proceed(correctAction(reducePrecedence.get))
      else proceed(correctAction(shiftPrecedence.get))

    case Accept =>
      true
  }

  def step(tokenStream: TokenStream) = {
    try {
      val todo = action(tokenStream.lookahead)
      execute(tokenStream, todo)
    } catch {
      case n: NoSuchElementException => {
        reset()
        throw UnexpectedException(
          tokenStream.lookahead,
          parseTable.expected(state))
      }
    }
  }
}

object Parser {
  def parsing(grammar: AugmentedGrammar) = {
    val parseTable = ParseTable.generate(grammar)
    new Parser(parseTable)
  }
}
