package de.adracus.elco.parser.core

import de.adracus.elco.grammar.core.{End, Grammar, Rule, Word}
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

  def action(token: Token) = {
    parseTable.action(state, wrap(token))
  }

  def wrap(token: Token) = {
    if (token.name == "EOF") End else Word(token.name)
  }

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
      result
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

  def step(tokenStream: TokenStream) = {
    try {
      val todo = action(tokenStream.lookahead)
      todo match {
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

        case Accept =>
          true
      }
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
  def parsing(grammar: Grammar) = {
    val parseTable = ParseTable.generate(grammar)
    new Parser(parseTable)
  }
}
