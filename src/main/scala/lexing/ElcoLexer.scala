package lexing

import lexing.MultiRegexConsumer.RichRegexConsumer

/**
 * Created by axel on 11/05/15.
 */
class ElcoLexer extends Lexer {
  on whitespace new WhitespaceConsumer()
  on strings new StringConsumer()

  append {
    new RegexConsumer("INTEGER", "[0-9]+", _.toInt) or
    new RegexConsumer("DOUBLE", "[0-9]*\\.[0-9]+", _.toDouble) or
    new RegexConsumer("IDENTIFIER", "[_a-zA-Z][_a-zA-Z0-9]{0,30}") or
    new RegexConsumer("CALL", "[.][_a-zA-Z][_a-zA-Z0-9]{0,30}", _.substring(1))
  }

  on symbol "{" consume()
  on symbol "}" consume()
  on symbol ")" consume()
  on symbol "(" consume()
}

object ElcoLexer {
  def main(args: Array[String]) {
    val lexer = new ElcoLexer()

    val iterator = lexer lex "watanga.test() 123.12 _test \"Moar test\""
    while (iterator.hasNext) {
      println(iterator.next())
    }
  }
}
