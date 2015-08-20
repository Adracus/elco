package de.adracus.elco.production

import de.adracus.elco.ast._
import de.adracus.elco.grammar.Grammar
import de.adracus.elco.production.expressions._


/**
 * Created by axel on 26/05/15.
 */
object ElcoGrammar extends Grammar {
  import Extractor._

  'L := 'L & 'Separator & 'E on2 A[ExpressionList]() % Ignore % A[Expression]() to((list, e) => list :+ e)

  'L := 'E on1 A[Expression]() to ExpressionList.single

  'Separator := ";" empty()
  'Separator := "NEWLINE" empty()

  'IdentifierList := "(" & ")" constant IdentifierList.empty()
  'IdentifierList := "(" & 'List & ")" at 1

  'List := "IDENTIFIER" & "," & 'List on2 Text % Ignore % A[IdentifierList]() to((ident, list) => list :+ ident)

  'List := "IDENTIFIER" on1 Text to(ident => IdentifierList(List(ident)))

  'Function := "fn" & "IDENTIFIER" & 'IdentifierList & "{" & 'L & "}" on3 Ignore % Text % A[IdentifierList]() % Ignore % A[ExpressionList]() to FunctionDefinition

  'InvokeList := 'E on1 A[Expression]() to InvokeList.single
  'InvokeList := 'E & "," & 'InvokeList on2 A[Expression]() % Ignore % A[InvokeList]() to((exp, list) => list :+ exp)

  'E := 'Call single()

  'E := "(" & 'E & ")" at 1

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

  'E := 'E & "IDENTIFIER" & 'E on3 A[Expression]() % Text % A[Expression]() to {(e1, name, e2) =>
    val fn = Extraction(e1, name)
    ExpressionCall(fn, InvokeList(List(e2)))
  }

  'E := 'E & "(" & 'InvokeList & ")" on2 A[Expression]() % Ignore % A[InvokeList]() to ExpressionCall

  'E := 'Conditional single()

  'E := "INTEGER" on1 IntNumber to Constant.apply

  'E := "pass" constant Unit

  'E := 'Function single()

  'E := 'Assignment single()

  'Assignment := "IDENTIFIER" & "=" & 'E on2 Text % Ignore % A[Expression]() to VarAssignment
  'Assignment := "IDENTIFIER" & ":=" & 'E on2 Text % Ignore % A[Expression]() to ValAssignment

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
