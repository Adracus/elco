package de.adracus.elco.parser

import de.adracus.elco.grammar._

import scala.collection.mutable

/**
 * Created by axel on 11/06/15.
 */
class FirstSet(grammar: ExtendedGrammar) {
  private def computeFirst(grammar: ExtendedGrammar) = {
    val table = new mutable.HashMap[ExtendedProducable, Set[Terminal]]()
      .withDefaultValue(Set.empty)
    var old: Map[ExtendedProducable, Set[Terminal]] = null
    val groupedRules = grammar.rules.groupBy(_.nonTerminal)

    def addToFirst(statement: ExtendedProducable, terminals: Statement*) = {
      table(statement) = table(statement) ++ terminals.map(_.asInstanceOf[Terminal])
    }

    def firstStep(nonTerminal: ExtendedNonTerminal, production: ExtendedProduction) = {
      def recurse(acc: Set[Terminal], statements: List[ExtendedProducable]): Unit = statements match {
        case Nil =>
          addToFirst(nonTerminal, (acc + Epsilon).toList:_*)

        case st :: tail =>
          val _first = table(st)
          if (!(_first contains Epsilon))
            addToFirst(nonTerminal, (acc ++ _first).toList:_*)
          else {
            recurse(acc ++ _first, tail)
          }
      }

      recurse(Set.empty, production.statements)
    }

    do {
      old = table.toMap

      grammar.statements.foreach {
        case t: ExtendedTerminal => table(t) = table(t) + t.base.asInstanceOf[Terminal]

        case nt: ExtendedNonTerminal =>
          groupedRules(nt) foreach {
            case rule if rule.production.isEpsilonProduction =>
              addToFirst(nt, Epsilon)

            case rule =>
              firstStep(nt, rule.production)
          }
      }
    } while (old != table)

    table.toMap
  }

  def firstStep(statements: List[ExtendedProducable]) = {
    def recurse(acc: Set[Terminal], statements: List[ExtendedProducable]): Set[Terminal] = statements match {
      case st :: tail =>
        val _first = table(st)
        if (_first.contains(Epsilon))
          recurse(acc ++ _first, tail)
        else
          _first

      case Nil => acc
    }

    recurse(Set.empty, statements)
  }

  val table = computeFirst(grammar)

  def apply(extendedProducable: ExtendedProducable): Set[Terminal] = this.apply(List(extendedProducable))
  def apply(iterable: Iterable[ExtendedProducable]): Set[Terminal] = firstStep(iterable.toList)
}

