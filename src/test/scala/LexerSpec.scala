import de.adracus.elco.lexer.consumer.{RegexConsumer, RegexIgnorer}
import de.adracus.elco.lexer.core.{Lexer, Position, Token, UnrecognizedSymbol}
import org.scalatest.{FunSpec, Matchers}

/**
 * Created by axel on 20/05/15.
 */
class LexerSpec extends FunSpec with Matchers {
  describe("Lexer") {
    describe("next") {
      it("should return the matched tokens") {
        object TestLexer extends Lexer {
          this addConsumer new RegexConsumer("[0-9]+", "INTEGER", Some(_.toInt))
          this addConsumer new RegexIgnorer(" ")
        }

        val stream = TestLexer.lex("123 0 0 9")

        val tokens = stream.toSeq
        val expected = Seq (
          Token("INTEGER", Position(0, 0), Some(123)),
          Token("INTEGER", Position(4, 0), Some(0)),
          Token("INTEGER", Position(6, 0), Some(0)),
          Token("INTEGER", Position(8, 0), Some(9)),
          Token("EOF", Position(9, 0))
        )
        assert(tokens == expected)
      }

      it("should throw an error on unrecognized tokens") {
        object TestLexer extends Lexer {
          this addConsumer new RegexConsumer("[0-9]+", "INTEGER", Some(_.toInt))
          this addConsumer new RegexIgnorer(" ")
        }

        val stream = TestLexer.lex("abc")
        val symbolError = intercept[UnrecognizedSymbol] {
          stream next()
        }

        assert(symbolError.position == Position(0, 0))
        assert(symbolError.symbol == "a")
      }
    }
  }
}
