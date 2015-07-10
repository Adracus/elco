package de.adracus.elco.grammar

import de.adracus.elco.ast._

import scala.collection.mutable.ListBuffer

import Extractor.{E1, E2, E3, E4, E5, E6}

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

  def transform(f: Seq[_] => AstNode) = Production(statements.toList, Some(f))

  def on[B1](e1: E1[B1]) = P1Builder(e1)

  def on[B1, B2](e2: E2[B1, B2]) = P2Builder(e2)

  def on[B1, B2, B3](e3: E3[B1, B2, B3]) = P3Builder(e3)

  def on[B1, B2, B3, B4](e4: E4[B1, B2, B3, B4]) = P4Builder(e4)

  def on[B1, B2, B3, B4, B5](e5: E5[B1, B2, B3, B4, B5]) = P5Builder(e5)

  def on[B1, B2, B3, B4, B5, B6](e6: E6[B1, B2, B3, B4, B5, B6]) = P6Builder(e6)

  case class P1Builder[B1](e1: E1[B1]) {
    def to(f: B1 => AstNode) =
      Production(statements.toList, Some((seq: Seq[_]) => f(e1(seq))))
  }

  case class P2Builder[B1, B2](e2: E2[B1, B2]) {
    def to(e2: E2[B1, B2])(f: (B1, B2) => AstNode) =
      Production(statements.toList, Some((seq: Seq[_]) => f.tupled(e2(seq))))
  }

  case class P3Builder[B1, B2, B3](e3: E3[B1, B2, B3]) {
    def to(f: (B1, B2, B3) => AstNode) =
      Production(statements.toList, Some((seq: Seq[_]) => f.tupled(e3(seq))))
  }

  case class P4Builder[B1, B2, B3, B4](e4: E4[B1, B2, B3, B4]) {
    def to(f: (B1, B2, B3, B4) => AstNode) =
      Production(statements.toList, Some((seq: Seq[_]) => f.tupled(e4(seq))))
  }

  case class P5Builder[B1, B2, B3, B4, B5](e5: E5[B1, B2, B3, B4, B5]) {
    def to(f: (B1, B2, B3, B4, B5) => AstNode) =
      Production(statements.toList, Some((seq: Seq[_]) => f.tupled(e5(seq))))
  }

  case class P6Builder[B1, B2, B3, B4, B5, B6](e6: E6[B1, B2, B3, B4, B5, B6]) {
    def to(f: (B1, B2, B3, B4, B5, B6) => AstNode) =
      Production(statements.toList, Some((seq: Seq[_]) => f.tupled(e6(seq))))
  }
}
