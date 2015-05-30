import de.adracus.elco.lexer.core.{Position, Unclosed, Hit, Lexer}
import de.adracus.elco.lexer.production.StringConsumer
import org.scalatest.{Matchers, FunSpec}

/**
 * Created by axel on 21/05/15.
 */
class StringConsumerSpec extends FunSpec with Matchers {
  describe("StringConsumer") {
    describe("tryMatch") {
      it("should match the string") {
        val consumer = new StringConsumer("\"", "\\", "\n")
        val lexer = new Lexer("\"Some test\"")

        val test = consumer.tryMatch(lexer)
        assert(test.isDefined)
        val matched = test.get
        assert(matched.isInstanceOf[Hit])
        val hit = matched.asInstanceOf[Hit]
        assert(hit == Hit("STRING", 11, Some("Some test")))
      }

      it("should match the string and remove escape characters") {
        val consumer = new StringConsumer("\"", "\\", "\n")
        val lexer = new Lexer("\"Some \\\"test\\\"\"")

        val test = consumer.tryMatch(lexer)
        assert(test.isDefined)
        val matched = test.get
        assert(matched.isInstanceOf[Hit])
        val hit = matched.asInstanceOf[Hit]
        assert(hit == Hit("STRING", 15, Some("Some \"test\"")))
      }

      it("should fail if the string is divided by newlines") {
        val consumer = new StringConsumer("\"", "\\", "\n")
        val lexer = new Lexer("\"Some \\\"test\\\"\n\"")

        val exception = intercept[Unclosed] {
          consumer.tryMatch(lexer)
        }

        assert(exception == Unclosed("String", Position(0, 0)))
      }

      it("should fail if the string is not closed") {
        val consumer = new StringConsumer("\"", "\\", "\n")
        val lexer = new Lexer("\"Some \\\"test\\\"")

        val exception = intercept[Unclosed] {
          consumer.tryMatch(lexer)
        }

        assert(exception == Unclosed("String", Position(0, 0)))
      }
    }
  }
}
