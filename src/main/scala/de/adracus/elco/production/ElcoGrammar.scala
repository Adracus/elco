package de.adracus.elco.production

import de.adracus.elco.grammar.Grammar


/**
 * Created by axel on 26/05/15.
 */
object ElcoGrammar extends Grammar {
  'L := 'E & 'Separator & 'L | 'E

  'Separator := ";" | "NEWLINE"

  'ArgList := "(" & ")" | "(" & 'List & ")"

  'List := "IDENTIFIER" & "," & 'List | "IDENTIFIER"

  'Function := "fn" & "IDENTIFIER" & 'ArgList & "{" & 'L & "}"

  'E := 'E & "+" & 'E

  'E := 'E & "==" & 'E

  'E := 'E & "^" & 'E

  'ClassDef := "class" & "IDENTIFIER" & "{" & 'L & "}"

  'Conditional := "if" & 'E & "{" & 'L & "}" & "else" & "{" & 'L & "}"

  'E := 'Conditional

  'E := "INTEGER"

  'E := "pass"

  'E := 'Function

  'E := 'ClassDef

  'E := "IDENTIFIER" & "=" & 'E

  'E := "IDENTIFIER"

  left("-")
  left("*")
  left("/")

  right("=")
  right("^")
}
