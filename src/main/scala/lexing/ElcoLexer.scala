package lexing

import lexing.MultiRegexConsumer.RichRegexConsumer
import lexing.LexerDSL.LexerWithDSL

/**
 * Created by axel on 11/05/15.
 */
class ElcoLexer extends Lexer {
  this.on whitespace new WhitespaceConsumer()
  this.on strings new StringConsumer()

  append {
    keywords("where", "if", "else", "while", "do", "break") or
    new RegexConsumer("INTEGER", "[0-9]+", _.toInt) or
    new RegexConsumer("DOUBLE", "[0-9]*\\.[0-9]+", _.toDouble) or
    new RegexConsumer("IDENTIFIER", "[_a-zA-Z][_a-zA-Z0-9]{0,30}") or
    new RegexConsumer("CALL", "[.][_a-zA-Z][_a-zA-Z0-9]{0,30}", _.substring(1))
  }

  this.on symbol "{" consume()
  this.on symbol "}" consume()
  this.on symbol ")" consume()
  this.on symbol "(" consume()
}

object ElcoLexer {
  def main(args: Array[String]) {
    val lexer = new ElcoLexer()

    val iterator = lexer lex "while(test){if(condition)(\"yeha\""
    while (iterator.hasNext) {
      println(iterator.next())
    }
  }
}
