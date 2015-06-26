import de.adracus.elco.lexer.core.{Position, Token}
import de.adracus.elco.production.ElcoLexer
import org.scalatest.{FunSpec, Matchers}

/**
 * Created by axel on 21/05/15.
 */
class ElcoLexerSpec extends FunSpec with Matchers {
  val lexer = ElcoLexer

  describe("ElcoLexer") {
    describe("next") {
      it("should lex the integer token") {
        val stream = lexer.lex("123")

        assert(stream.next() == Token("INTEGER", Position(0, 0), Some(123)));
      }

      it("should lex the double token") {
        val stream = lexer.lex("123.456")

        assert(stream.next() == Token("DOUBLE", Position(0, 0), Some(123.456)))
      }

      it("should ignore whitespace and lex the tokens") {
        val stream = lexer.lex("123 45.6")
        val tokens = stream.toSeq

        assert(tokens == Seq(
          Token("INTEGER", Position(0, 0), Some(123)),
          Token("DOUBLE", Position(4, 0), Some(45.6)),
          Token("EOF", Position(8, 0))
        ))
      }

      it("should parse the sequence properly") {
        val stream = lexer.lex("test = 3; \n 5 + 6")
        val tokens = stream.toSeq

        val assertion = Seq(
          Token("IDENTIFIER", Position(0, 0), Some("test")),
          Token("=", Position(5, 0)),
          Token("INTEGER", Position(7, 0), Some(3)),
          Token(";", Position(8, 0)),
          Token("NEWLINE", Position(10, 0)),
          Token("INTEGER", Position(1, 1), Some(5)),
          Token("+", Position(3, 1)),
          Token("INTEGER", Position(5, 1), Some(6)),
          Token("EOF", Position(6, 1))
        )

        assert(tokens == assertion)
      }
    }
  }
}
