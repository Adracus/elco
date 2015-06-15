package de.adracus.elco.parser

import scala.collection.mutable

/**
 * Created by axel on 14/06/15.
 */
class Enumerator[A] {
  private var ct = 0
  private val table = new mutable.HashMap[A, Int]()

  def apply(key: A) = {
    val entry = table.get(key)
    if (entry.isDefined) entry.get
    else {
      table(key) = ct
      ct += 1
      ct - 1
    }
  }
}
