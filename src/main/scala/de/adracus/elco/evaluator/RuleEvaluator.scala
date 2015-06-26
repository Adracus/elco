package de.adracus.elco.evaluator

import de.adracus.elco.grammar.Rule

/**
 * Created by axel on 26/06/15.
 */
class RuleEvaluator(val rule: Rule, val evaluation: Seq[Any] => Any) {

  def canEqual(other: Any): Boolean = other.isInstanceOf[RuleEvaluator]

  override def equals(other: Any): Boolean = other match {
    case that: RuleEvaluator =>
      (that canEqual this) &&
        rule == that.rule
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(rule)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}