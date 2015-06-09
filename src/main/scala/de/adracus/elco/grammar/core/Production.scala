package de.adracus.elco.grammar.core

/**
 * Created by axel on 26/05/15.
 */
case class Production(statements: Seq[Producable]) extends Iterable[Producable] {
  require(!statements.isEmpty, message = "At least one production has to be present")
  require(statements.filter(_ == Epsilon).lengthCompare(2) < 0, message = "Epsilon can only be present once")

  def apply(n: Int) = statements(n)

  def contains(statement: Statement) = statements.contains(statement)

  def and(production: Production) = Production(statements ++ production.statements)
  def and(producable: Producable) = Production(statements :+ producable)
  def &(production: Production) = and(production)
  def &(producable: Producable) = and(producable)

  def or(production: Production) = new ProductionList(this, production)
  def or(producable: Producable) = new ProductionList(this, Production(producable))
  def |(production: Production) = or(production)
  def |(producable: Producable) = or(producable)

  override def toString = statements mkString " & "

  override def iterator: Iterator[Statement] = statements.iterator

  def length = statements.length

  def isEpsilonProduction = Epsilon :: Nil == statements

  def startsWithTerminal = {
    if (statements.isEmpty) false
    else statements.head.isInstanceOf[Terminal]
  }

  def terminalsAfter(nonTerminal: NonTerminal) = {
    def inner(acc: Set[Terminal], remaining: List[Statement]): Set[Terminal] = remaining match {
      case a :: next :: tail =>
        if (a == nonTerminal && next.isInstanceOf[Terminal]) inner(acc + next.asInstanceOf[Terminal], tail)
        else inner(acc, next +: tail)
      case _ => acc
    }

    inner(Set.empty, statements.toList)
  }
}

object Production {
  def terminal(string: String) = Production(Seq(Word(string)))
  def nonTerminal(string: String) = Production(Seq(NonTerminal(string)))
}