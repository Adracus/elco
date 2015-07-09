package de.adracus.elco.grammar

import de.adracus.elco.ast._

import scala.collection.mutable.ListBuffer

/**
 * Created by axel on 07/07/15.
 */
class ProductionBuilder(initialStatements: Producable*) {
  private val statements = new ListBuffer[Producable]
  statements ++= initialStatements
  
  def &(statement: Producable) = {
    statements += statement
    this
  }

  def constant(node: AstNode) = Production(statements.toList, Some(_ => node))

  def at(idx: Int) = Production(statements.toList, Some(_.apply(idx).asInstanceOf[AstNode]))

  def single(): Production = Production(statements.toList, Some(_.ensuring(_.lengthCompare(1) == 0).head.asInstanceOf[AstNode]))

  def head() = at(0)

  def last() = Production(statements.toList, Some(_.last.asInstanceOf[AstNode]))

  def on[A, B](matcher: Extractor1[A, B]) =
    new ReductionBuilder1(matcher)

  def on[A1, A2, B1, B2](matcher: Extractor2[A1, A2, B1, B2]) =
    new ReductionBuilder2[A1, A2, B1, B2](matcher)

  def on[A1, A2, A3, B1, B2, B3](matcher: Extractor3[A1, A2, A3, B1, B2, B3]) =
    new ReductionBuilder3[A1, A2, A3, B1, B2, B3](matcher)

  def on[A1, A2, A3, A4, B1, B2, B3, B4](matcher: Extractor4[A1, A2, A3, A4, B1, B2, B3, B4]) =
    new ReductionBuilder4[A1, A2, A3, A4, B1, B2, B3, B4](matcher)

  def on[A1, A2, A3, A4, A5, B1, B2, B3, B4, B5](matcher: Extractor5[A1, A2, A3, A4, A5, B1, B2, B3, B4, B5]) =
    new ReductionBuilder5[A1, A2, A3, A4, A5, B1, B2, B3, B4, B5](matcher)

  case class ReductionBuilder1[A, B](matcher: Extractor1[A, B]) {
    def transform(transform: B => AstNode) = {
      Production(statements.toList, Some({
        case Seq(a: A) =>
          transform(matcher.extract(a))
      }))
    }
  }

  case class ReductionBuilder2[A1, A2, B1, B2](
      matcher: Extractor2[A1, A2, B1, B2]) {
    def transform(transform: (B1, B2) => AstNode) = {
      Production(statements.toList, Some({
        case Seq(a: A1, b: A2) =>
          transform.tupled(matcher.extract(a, b))
      }))
    }
  }

  case class ReductionBuilder3[A1, A2, A3, B1, B2, B3](
      matcher: Extractor3[A1, A2, A3, B1, B2, B3]) {
    def transform(transform: (B1, B2, B3) => AstNode) = {
      Production(statements.toList, Some({
        case Seq(a: A1, b: A2, c: A3) =>
          transform.tupled(matcher.extract(a, b, c))
      }))
    }
  }

  case class ReductionBuilder4[A1, A2, A3, A4, B1, B2, B3, B4](
      matcher: Extractor4[A1, A2, A3, A4, B1, B2, B3, B4]) {
    def transform(transform: (B1, B2, B3, B4) => AstNode) = {
      Production(statements.toList, Some({
        case Seq(a: A1, b: A2, c: A3, d: A4) =>
          transform.tupled(matcher.extract(a, b, c, d))
      }))
    }
  }

  case class ReductionBuilder5[A1, A2, A3, A4, A5, B1, B2, B3, B4, B5](
      matcher: Extractor5[A1, A2, A3, A4, A5, B1, B2, B3, B4, B5]) {
    def transform(transform: (B1, B2, B3, B4, B5) => AstNode) = {
      Production(statements.toList, Some({
        case Seq(a: A1, b: A2, c: A3, d: A4, e: A5) =>
          transform.tupled(matcher.extract(a, b, c, d, e))
      }))
    }
  }
}
