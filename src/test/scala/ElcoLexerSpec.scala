import de.adracus.elco.lexer.core.{Position, Token}
import de.adracus.elco.production.ElcoLexer
import org.scalatest.{FunSpec, Matchers}

/**
 * Created by axel on 21/05/15.
 */
class ElcoLexerSpec extends FunSpec with Matchers {
  describe("ElcoLexer") {
    describe("next") {
      it("should lex the integer token") {
        val lexer = new ElcoLexer

        val stream = lexer.lex("123")

        assert(stream.next() == Token("INTEGER", Position(0, 0), Some(123)));
      }

      it("should lex the double token") {
        val lexer = new ElcoLexer()

        val stream = lexer.lex("123.456")

        assert(stream.next() == Token("DOUBLE", Position(0, 0), Some(123.456)))
      }

      it("should ignore whitespace and lex the tokens") {
        val lexer = new ElcoLexer()

        val stream = lexer.lex("123 45.6")
        val tokens = stream.toSeq

        assert(tokens == Seq(
          Token("INTEGER", Position(0, 0), Some(123)),
          Token("DOUBLE", Position(4, 0), Some(45.6)),
          Token("EOF", Position(8, 0))
        ))
      }
    }
  }
}
