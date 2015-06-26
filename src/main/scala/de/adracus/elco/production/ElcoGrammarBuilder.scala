package de.adracus.elco.production

import de.adracus.elco.grammar.GrammarBuilder

import scala.collection.mutable


/**
 * Created by axel on 26/05/15.
 */
object ElcoGrammarBuilder extends GrammarBuilder {
  val methods = Map("+" -> ((a: Int, b: Int) => a + b))

  val values = new mutable.HashMap[String, Any]()
  'L := 'E & ";" & 'L | 'E & "NEWLINE" & 'L | 'E

  'E := 'E & "+" & 'E evaluate {
    case Seq(a: Int, b: Int) => methods("+")(a, b)
  }

  'E := 'E & "^" & 'E evaluate {
    case Seq(a: Int, b: Int) => Math.pow(a.toDouble, b.toDouble).toInt
  }

  'E := "INTEGER"

  'E := "IDENTIFIER" & "=" & 'E evaluate {
    case Seq(name: String, value) =>
      values(name) = value
      value
  }

  'E := "IDENTIFIER" evaluate {
    case Seq(name: String) =>
      val value = values.get(name)
      if (value.isEmpty) throw new Exception(s"Undefined variable '$name'")
      else value.get
  }

  left("-")
  left("*")
  left("/")

  right("=")
  right("^")
}
