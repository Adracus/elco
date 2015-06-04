package de.adracus.elco.parser

import scala.collection.mutable

/**
 * Created by axel on 04/06/15.
 */
class ItemSetEnumerator {
  private val itemSets = new mutable.HashMap[ItemSet, Int]()

  def put(itemSet: ItemSet) = {
    val length = itemSets.size
    if (!itemSets.contains(itemSet)) {
      itemSets(itemSet) = length
    }
  }

  def getNumber(itemSet: ItemSet) = itemSets(itemSet)
}
