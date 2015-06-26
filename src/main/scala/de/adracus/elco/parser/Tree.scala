package de.adracus.elco.parser

import de.adracus.elco.grammar.Rule
import de.adracus.elco.lexer.core.Token

/**
 * Created by axel on 15/06/15.
 */
sealed trait Tree {
  def name: String

  def toFormattedInner: List[String]

  def selfStr(indent: Int, atEnd: Boolean) = {

  }
}

case class Leaf(token: Token) extends Tree {
  def name = token.name

  def toFormattedInner = List(name)
}

case class Node(rule: Rule, children: Seq[Tree]) extends Tree {
  def name = rule.nonTerminal.name

  /*
  E
  ├──S
  │  ├──INT
  │  └──INT
  └──END
   */

  def toFormattedInner = {
    val childLines = children.map(_.toFormattedInner).toList
    List(name) ++ childLines.lastMap({lines =>
      lines.firstMap("├── " + _, "│   " +_)
    }, { lastLines =>
      lastLines.firstMap("└── " + _, "    " + _)
    }).flatten
  }

  def toFormatted = {
    toFormattedInner.mkString("\n")
  }

  private implicit class RichIterable[A](val iterable: Iterable[A]) {
    def lastMap[B](mapper: A => B, onLast: A => B) = {
      def recurse(acc: List[B], current: A, iterator: Iterator[A]): List[B] = {
        if (!iterator.hasNext) acc :+ onLast(current)
        else recurse(acc :+ mapper(current), iterator.next(), iterator)
      }

      val iterator = iterable.iterator
      recurse(List.empty, iterator.next(), iterator)
    }

    def firstMap[B](onFirst: A => B, mapper: A => B) = {
      def recurse(acc: List[B], iterator: Iterator[A]): List[B] = {
        if (iterator.hasNext) recurse(acc :+ mapper(iterator.next()), iterator)
        else acc
      }

      val iterator = iterable.iterator
      recurse(List(onFirst(iterator.next())), iterator)
    }
  }
}
