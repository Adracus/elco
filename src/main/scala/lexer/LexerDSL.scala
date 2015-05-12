package lexer

/**
 * Created by axel on 11/05/15.
 */
object LexerDSL {
  protected class LexingBehaviour(lexer: Lexer) {
    protected class RegexLexingBehaviour(val regex: String) {
      def makeTokenWithIdentifier(identifier: String) = lexer.append(new RegexConsumer(identifier, regex))
      def ignore() = lexer.append(new Ignorer(regex))
    }

    protected class SymbolLexingBehaviour(val symbol: String) {
      def consume() = lexer.append(new SymbolConsumer(symbol))
    }

    def symbol(symbol: String) = new SymbolLexingBehaviour(symbol)
    def whitespace(whitespaceConsumer: WhitespaceConsumer) = lexer.append(new WhitespaceConsumer())
    def regex(regex: String) = new RegexLexingBehaviour(regex)
    def strings(stringConsumer: StringConsumer) = lexer.append(new StringConsumer())
  }

  implicit class LexerWithDSL(val lexer: Lexer) {
    def on = new LexingBehaviour(lexer)
  }
}
