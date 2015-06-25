package de.adracus.elco.grammar.production

import de.adracus.elco.grammar.core.GrammarBuilder

import scala.collection.mutable


/**
 * Created by axel on 26/05/15.
 */
object ElcoGrammarBuilder extends GrammarBuilder {
  val values = new mutable.HashMap[String, Any]()
  'L := 'E

  'E := 'E & "+" & 'E evaluate {
    case Seq(a: Int, b: Int) => a + b
  }

  'E := 'E & "^" & 'E evaluate {
    case Seq(a: Int, b: Int) => Math.pow(a.toDouble, b.toDouble).toInt
  }

  'E := "INTEGER"

  left("=")
  left("+")
  left("-")
  left("*")
  left("/")

  right("^")
}
