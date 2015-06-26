package de.adracus.elco.production

import de.adracus.elco.grammar.Grammar


/**
 * Created by axel on 26/05/15.
 */
object ElcoGrammar extends Grammar {
  'L := 'E & ";" & 'L | 'E & "NEWLINE" & 'L | 'E

  'E := 'E & "+" & 'E

  'E := 'E & "^" & 'E

  'E := "INTEGER"

  'E := "IDENTIFIER" & "=" & 'E

  'E := "IDENTIFIER"

  left("-")
  left("*")
  left("/")

  right("=")
  right("^")
}
