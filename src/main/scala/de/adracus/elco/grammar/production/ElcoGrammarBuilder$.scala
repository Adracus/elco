package de.adracus.elco.grammar.production

import de.adracus.elco.grammar.core.{Epsilon, GrammarBuilder}

/**
 * Created by axel on 26/05/15.
 */
object ElcoGrammarBuilder$ extends GrammarBuilder {
  'List      := 'Statement & 'List | Epsilon
  'Statement := 'Number & "+" & 'Number
  'Number    := "INTEGER" | "DOUBLE"
}
