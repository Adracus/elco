package de.adracus.elco.parser.production

import de.adracus.elco.parser.core.{Epsilon, Grammar}

/**
 * Created by axel on 26/05/15.
 */
class ElcoGrammar extends Grammar {
  'E := 'T & 'Et

  'Et := "+" & 'T & 'Et | Epsilon

  'T := 'F & 'Tt

  'Tt := "*" & 'F & 'Tt | Epsilon

  'F := "(" & 'E & ")"

  'F := "id"
}

object ElcoGrammar extends App {
  val g = new ElcoGrammar

  println(g.first)
  println(g.follow)
}
