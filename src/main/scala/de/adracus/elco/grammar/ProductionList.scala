package de.adracus.elco.grammar

/**
 * Created by axel on 26/05/15.
 */
class ProductionList(val productions: Production*) extends Iterable[Production] {
  override def toString() = productions.mkString(" | ")

  def contains(producable: Producable) = productions.exists(_.contains(producable))

  def or(other: ProductionList): ProductionList = new ProductionList(productions ++ other.productions:_*)
  def or(production: Production): ProductionList = or(new ProductionList(production))
  def or(producable: Producable): ProductionList = or(Production(List(producable)))

  def | (other: ProductionList): ProductionList = or(other)
  def | (production: Production): ProductionList = or(production)
  def | (producable: Producable): ProductionList = or(producable)

  override def iterator = productions.iterator
}