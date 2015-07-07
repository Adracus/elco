package de.adracus.elco.evaluator

import de.adracus.elco.grammar._
import de.adracus.elco.parser.{Leaf, Node, Tree}


import scala.collection.mutable

/**
 * Created by axel on 26/06/15.
 */
abstract class Evaluator[A, B] extends ProductionDSL {
  private val evaluators = new mutable.HashMap[Rule, RuleEvaluator]()

  val scope = new Scope[A, B]

  case class Caller[T>:Null<:AnyRef](clazz: T) {
    def call(methodName:String,args:AnyRef*):AnyRef = {
      def argTypes = args.map(_.getClass)
      def method = clazz.getClass.getMethod(methodName, argTypes: _*)
      method.invoke(clazz,args: _*)
    }
  }

  implicit def anyToCallable[T>:Null<:AnyRef](clazz: T):Caller[T] = new Caller(clazz)

  private def set(ruleEvaluator: RuleEvaluator) = evaluators(ruleEvaluator.rule) = ruleEvaluator

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
      set(new RuleEvaluator(rule, evalWrapper))
    }

    def constant(any: Any): Any = {
      set(new RuleEvaluator(rule, _ => any))
    }

    protected class IfBuilder(val rule: Rule, val condition: Int) {
      private var _then: Option[Int] = None

      def Then(n: Int) = {
        assert(_then.isEmpty)
        _then = Some(n)
        set(new RuleEvaluator(rule, { seq =>
          if (e[Boolean](seq(condition))) {
            e[Any](seq(_then.get))
          }
        }))
        this
      }

      def Else(_else: Int): Unit = {
        set(new RuleEvaluator(rule, { seq =>
          if (e[Boolean](seq(condition))) {
            e[Any](seq(_then.get))
          } else {
            e[Any](seq(_else))
          }
        }))
      }
    }

    def If(n: Int) = new IfBuilder(rule, n)

    def unit(): Unit = {
      val evaluator = (defaultEvaluator _).andThen(_ => Unit)
      set(new RuleEvaluator(rule, evaluator))
    }

    def asLast(): Any = {
      val evaluator = (defaultEvaluator _).andThen(_.last)
      set(new RuleEvaluator(rule, evaluator))
    }

    def at(n: Int): Any = {
      val evaluator = (defaultEvaluator _).andThen(_.apply(n))
      set(new RuleEvaluator(rule, evaluator))
    }

    def apply(evaluation: Seq[Any] => Any) = {
      set(new RuleEvaluator(rule, evaluation))
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

  protected def defaultEvaluator(input: Seq[Any]): Seq[Any] = {
    input map {
      case f: (() => Any) => unwrap(f)

      case default => default
    }
  }
}
