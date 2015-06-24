package de.adracus.elco.grammar.production

import de.adracus.elco.grammar.core.GrammarBuilder

/**
 * Created by axel on 26/05/15.
 */
object ElcoGrammarBuilder extends GrammarBuilder {
  'S := 'E evaluate { sub =>
    sub.head
  }
  'E := 'E & "+" & 'E evaluate { sub =>
    sub.head.asInstanceOf[Int] + sub.last.asInstanceOf[Int]
  }

  'E := "INTEGER" evaluate { sub =>
    sub.head
  }


  left("+")
  left("-")
  left("*")
  left("/")

  right("^")
}
