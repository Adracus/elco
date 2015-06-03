package de.adracus.elco.grammar.core

/**
 * Created by axel on 26/05/15.
 */
case class Production(statements: Statement*) extends Iterable[Statement] {
  require(!statements.isEmpty, message = "At least one production has to be present")
  require(statements.filter(_ == Epsilon).lengthCompare(2) < 0, message = "Epsilon can only be present once")

  def apply(n: Int) = statements(n)

  def contains(statement: Statement) = statements.contains(statement)

  def and(production: Production) = Production(statements ++ production.statements:_*)
  def and(statement: Statement) = Production(statements :+ statement:_*)
  def &(production: Production) = and(production)
  def &(statement: Statement) = and(statement)

  def or(production: Production) = new ProductionList(this, production)
  def or(statement: Statement) =  new ProductionList(this, Production(statement))
  def |(production: Production) = or(production)
  def |(statement: Statement) = or(statement)

  override def toString = statements mkString " & "

  override def iterator: Iterator[Statement] = statements.iterator

  def length = statements.length

  def isEpsilonProduction = Epsilon :: Nil == statements

  def startsWithTerminal = {
    if (statements.isEmpty) false
    else statements.head.isInstanceOf[BaseTerminal]
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
  def terminal(string: String) = Production(Terminal(string))
  def nonTerminal(string: String) = Production(NonTerminal(string))
}