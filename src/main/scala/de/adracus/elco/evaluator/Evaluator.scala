package de.adracus.elco.evaluator

import de.adracus.elco.grammar._
import de.adracus.elco.parser.{Leaf, Node, Tree}

import scala.collection.mutable

/**
 * Created by axel on 26/06/15.
 */
class Evaluator extends ProductionDSL {
  private val evaluators = new mutable.HashMap[Rule, RuleEvaluator]()

  val scope = new Scope

  private def add(ruleEvaluator: RuleEvaluator) = evaluators(ruleEvaluator.rule) = ruleEvaluator

  protected def unwrap(f: () => Any): Any = {
    val unwrapped = f()
    unwrapped match {
      case u: (() => Any) => unwrap(u)
      case default => default
    }
  }

  protected def e[A](any: Any) = any match {
    case f: (() => Any) => unwrap(f).asInstanceOf[A]
    case default => default.asInstanceOf[A]
  }

  protected class RuleBuilder(val nonTerminal: NonTerminal) {
    def :=(production: Production) = Rule(nonTerminal, production)
  }

  implicit def symbolToRuleBuilder(symbol: Symbol): RuleBuilder = new RuleBuilder(NonTerminal(symbol.name))

  implicit def ruleToEvaluationBuilder(rule: Rule): EvaluatorBuilder = new EvaluatorBuilder(rule)

  protected class EvaluatorBuilder(val rule: Rule) {
    def eval(evaluation: Seq[Any] => Any): Unit = {
      val evalWrapper = (values: Seq[Any]) => {
        evaluation(values.map{
          case f: (() => Any) => unwrap(f)
          case default => default
        })
      }
      add(new RuleEvaluator(rule, evalWrapper))
    }

    def apply(evaluation: Seq[Any] => Any) = {
      add(new RuleEvaluator(rule, evaluation))
    }
  }

  def eval(node: Node): () => Any =  {
    val inner = innerEval(node)
    inner.asInstanceOf[() => Any]
  }

  def innerEval(tree: Tree): Any = tree match {
    case leaf: Leaf =>
      leaf.token.value.getOrElse(Unit)

    case n: Node =>
      val subEvaluations = n.children.map(innerEval)
      () => evaluators.get(n.rule)
        .map(_.evaluation)
        .getOrElse(defaultEvaluation(n.rule) _)
        .apply(subEvaluations)
  }

  protected def defaultEvaluation(rule: Rule)(input: Seq[Any]): Any = {
    println(s"No evaluation defined for '$rule', applying default")
    defaultEvaluator(input)
  }

  protected def defaultEvaluator(input: Seq[Any]): Any = {
    input map {
      case f: (() => Any) => unwrap(f)

      case default => default
    }
  }
}
