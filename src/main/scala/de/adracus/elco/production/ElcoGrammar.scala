package de.adracus.elco.production

import de.adracus.elco._
import de.adracus.elco.ast._
import de.adracus.elco.grammar.{Word, NonTerminal, ProductionBuilder, Grammar}


/**
 * Created by axel on 26/05/15.
 */
object ElcoGrammar extends Grammar {
  import Extractor._

  'L := 'E & 'Separator & 'L on2 A[Expression]() % Ignore % A[ExpressionList]() to((e, list) => list :+ e)

  'L := 'E on1 A[Expression]() to ExpressionList.single

  'Separator := ";" empty()
  'Separator := "NEWLINE" empty()

  'ArgList := "(" & ")" constant ArgList.empty()
  'ArgList := "(" & 'List & ")" at 1

  'List := "IDENTIFIER" & "," & 'List on2 Text % Ignore % A[IdentifierList]() to((ident, list) => list :+ ident)

  'List := "IDENTIFIER" on1 Text to(ident => IdentifierList(List(ident)))

  'Function := "fn" & "IDENTIFIER" & 'ArgList & "{" & 'L & "}" on3 Ignore % Text % A[ArgList]() % Ignore % A[ExpressionList]() to FunctionDefinition

  'InvokeList := 'E on1 A[Expression]() to InvokeList.single
  'InvokeList := 'E & "," & 'InvokeList on2 A[Expression]() % Ignore % A[InvokeList]() to((exp, list) => list :+ exp)

  'E := 'Call single()

  'Call := "IDENTIFIER" & "(" & 'InvokeList & ")" on2 Text % Ignore % A[InvokeList]() to FunctionCall.apply
  'Call := "IDENTIFIER" & "(" & ")" on1 Text to FunctionCall.zeroArg

  'Conditional := "when" & 'E & 'Wrapped on2 Ignore % A[Expression]() % A[Expression]() to {(cond, ifBody) =>
    Conditional(cond, ifBody, None)
  }

  'Conditional := "if" & 'E & 'Wrapped & "else" & 'Wrapped on3
      Ignore % A[Expression]() % A[Expression]() % Ignore % A[Expression]() to {(cond, ifBody, elseBody) =>
    Conditional(cond, ifBody, Some(elseBody))
  }

  'Conditional := "if" & 'E & 'Wrapped on2 Ignore % A[Expression]() % A[Expression]() to {(e1, e2) =>
    Conditional(e1, e2, None)
  }

  'Wrapped := ":" & 'E last()

  'Wrapped := "{" & 'L & "}" at 1

  'E := 'E & "." & "IDENTIFIER" on2 A[Expression]() % Ignore % Text to Extraction

  'E := 'Conditional single()

  'E := "INTEGER" on1 IntNumber to Constant

  'E := "pass" constant Unit

  'E := 'Function single()

  'E := 'Assignment single()

  'Assignment := "IDENTIFIER" & "=" & 'E on2 Text % Ignore % A[Expression]() to VarAssignment

  'E := "IDENTIFIER" on1 Text to VariableAccess

  left("PLUS_OP")
  left("MINUS_OP")
  left("MUL_OP")
  left("DIV_OP")

  right("=")
  right(":=")
  right("EQUALS_OP")
  right("POW_OP")
}
