package de.adracus.elco.parser.core

/**
 * Created by axel on 26/05/15.
 */
case class Production(statements: Statement*) {
  def and(production: Production) = Production((statements ++ production.statements):_*)
  def and(statement: Statement) = Production((statements :+ statement):_*)
  def &(production: Production) = and(production)
  def &(statement: Statement) = or(statement)

  def or(production: Production) = new ProductionList(this, production)
  def or(statement: Statement) =  new ProductionList(this, Production(statement))
  def |(production: Production) = or(production)
  def |(statement: Statement) = or(statement)

  override def toString = statements mkString " & "
}

object Production {
  def terminal(string: String) = Production(new Terminal(string))
}