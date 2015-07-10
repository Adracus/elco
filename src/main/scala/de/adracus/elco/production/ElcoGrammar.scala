package de.adracus.elco.production

import de.adracus.elco.ast._
import de.adracus.elco.ast_nodes._
import de.adracus.elco.grammar.Grammar


/**
 * Created by axel on 26/05/15.
 */
object ElcoGrammar extends Grammar {
  import Extractor._

  'L := 'E & 'Separator & 'L on IntNumber to Constant

  'L := 'E

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

  'Conditional := "if" & 'E & 'Wrapped

  'Wrapped := ":" & 'E last()

  'Wrapped := "{" & 'L & "}" at 1

  'E := 'Conditional single()

  'E := "INTEGER" on IntNumber transform Constant

  'E := "pass" constant Unit

  'E := 'Function single()

  'E := 'ClassDef single()

  'E := 'Assignment single()

  'Assignment := "IDENTIFIER" & ":=" & 'E on A[Identifier]() % Ignore % A[Expression]() transform {
    case (identifier, _, expression) => ValAssignment(identifier, expression)
  }

  'Assignment := "IDENTIFIER" & "=" & 'E on A[Identifier]() % Ignore % A[Expression]() transform {
    case (identifier, _, expression) => ValAssignment(identifier, expression)
  }

  'E := "IDENTIFIER" on Text transform Identifier

  left("PLUS_OP")
  left("MINUS_OP")
  left("MUL_OP")
  left("DIV_OP")

  right("=")
  right(":=")
  right("EQUALS_OP")
  right("POW_OP")
}
