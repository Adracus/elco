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

  'ExpList := 'E | 'E & "," & 'ExpList

  'E := 'E & "PLUS_OP" & 'E

  'E := 'E & "MINUS_OP" & 'E

  'E := 'Call

  'Call := "IDENTIFIER" & "(" & 'ExpList & ")" | "IDENTIFIER" & "(" & ")"

  'E := 'E & "COMPARE_OP" & 'E

  'E := 'E & "^" & 'E

  'ClassDef := "class" & "IDENTIFIER" & "{" & 'L & "}"

  'Conditional := "when" & 'E & "{" & 'L & "}"

  'Conditional := "if" & 'E & 'Wrapped & "else" & 'Wrapped

  'Wrapped := ":" & 'E | "{" & 'L & "}"

  'E := 'Conditional

  'E := "INTEGER"

  'E := "pass"

  'E := 'Function

  'E := 'ClassDef

  'E := "IDENTIFIER" & "=" & 'E

  'E := "IDENTIFIER"

  left("PLUS_OP")
  left("MINUS_OP")
  left("MUL_OP")
  left("DIV_OP")

  right("EQUALS_OP")
  right("POW_OP")
}
