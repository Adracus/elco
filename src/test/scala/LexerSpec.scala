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
        val lexer = new Lexer("123 0 0 9")
        lexer addConsumer new RegexConsumer("[0-9]+", "INTEGER", Some(_.toInt))
        lexer addConsumer new RegexIgnorer(" ")

        val tokens = lexer.toSeq
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
        val lexer = new Lexer("abc")
        lexer addConsumer new RegexConsumer("[0-9]+", "INTEGER", Some(_.toInt))
        lexer addConsumer new RegexIgnorer(" ")

        val symbolError = intercept[UnrecognizedSymbol] {
          lexer next()
        }

        assert(symbolError.position == Position(0, 0))
        assert(symbolError.symbol == "a")
      }
    }
  }
}
