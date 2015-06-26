package de.adracus.elco.grammar

/**
 * Created by axel on 24/06/15.
 */
sealed trait PrecedenceType extends Ordered[PrecedenceType] {
  val importance: Int

  override def compare(that: PrecedenceType): Int = importance - that.importance
}

object Left extends PrecedenceType {
  override val importance = 0
}

object Right extends PrecedenceType {
  override val importance = 1
}

object Unary extends PrecedenceType {
  override val importance = 2
}
