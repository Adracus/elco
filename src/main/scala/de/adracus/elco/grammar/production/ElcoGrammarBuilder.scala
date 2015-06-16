package de.adracus.elco.grammar.production

import de.adracus.elco.grammar.core.GrammarBuilder

/**
 * Created by axel on 26/05/15.
 */
object ElcoGrammarBuilder extends GrammarBuilder {
  'List      := 'Statement & 'List | 'Statement

  'Statement := 'Statement & "IDENTIFIER" & 'Statement | 'Number | "STRING"

  'Statement := "if" & 'Statement & 'Statement & "else" & 'Statement | "when" & 'Statement & 'Statement

  'Number    := "INTEGER" | "DOUBLE"
}
