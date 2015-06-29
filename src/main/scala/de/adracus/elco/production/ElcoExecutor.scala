package de.adracus.elco.production

import de.adracus.elco.parser.Executor

/**
 * Created by axel on 26/06/15.
 */
object ElcoExecutor extends Executor(ElcoLexer, ElcoGrammar, new ElcoEvaluator()) with App {
  println(evaluate("fn fibo(n) {if n == 1 {1} else {if n == 2 {1} else { fibo(n - 1) + fibo(n - 2)}}}; fibo(9)", printTree = true)())
}
