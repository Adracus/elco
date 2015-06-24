package de.adracus.elco.grammar.production

import de.adracus.elco.grammar.core.GrammarBuilder

/**
 * Created by axel on 26/05/15.
 */
object ElcoGrammarBuilder extends GrammarBuilder {
  'L := 'S & 'L | 'S
  'S := 'E
  'E := 'E & "+" & 'E | 'E & "-" & 'E | 'E & "*" & 'E | 'E & "/" & 'E | "INTEGER"

  left("+")
  left("-")
  left("*")
  left("/")
}
