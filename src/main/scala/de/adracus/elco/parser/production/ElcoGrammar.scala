package de.adracus.elco.parser.production

import de.adracus.elco.parser.core.Grammar

/**
 * Created by axel on 26/05/15.
 */
class ElcoGrammar extends Grammar {
  'If := "if" & "body"
  'List := 'Statement & 'List | 'Statement
  'Test := "if" | "else"
}

object ElcoGrammar extends App {
  val g = new ElcoGrammar

  println(g.toString)
}
