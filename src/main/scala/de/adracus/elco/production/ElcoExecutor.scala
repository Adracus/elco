package de.adracus.elco.production

import de.adracus.elco.parser.Executor

/**
 * Created by axel on 26/06/15.
 */
object ElcoExecutor extends Executor(ElcoLexer, ElcoGrammar, new ElcoEvaluator()) with App {
  val stream = ElcoLexer.lex("val=1\nval=val+5").toList.map(_.name)
  
  println(evaluate("val = 1; val = val + 5"))
}
