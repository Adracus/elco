package de.adracus.elco.grammar.core

/**
 * Created by axel on 24/06/15.
 */
class Precedence(val pType: PrecedenceType, val string: String, val importance: Int) extends Ordered[Precedence] {
  override def compare(that: Precedence): Int = {
    val typeCompare = pType.compare(that.pType)
    if (0 != typeCompare) typeCompare
    else importance - that.importance
  }


  def canEqual(other: Any): Boolean = other.isInstanceOf[Precedence]

  override def equals(other: Any): Boolean = other match {
    case that: Precedence =>
      (that canEqual this) &&
        pType == that.pType &&
        string == that.string
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(pType, string)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
