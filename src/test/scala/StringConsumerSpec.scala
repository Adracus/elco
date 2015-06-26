import de.adracus.elco.lexer.consumer.StringConsumer
import de.adracus.elco.lexer.core._
import org.scalatest.{FunSpec, Matchers}

/**
 * Created by axel on 21/05/15.
 */
class StringConsumerSpec extends FunSpec with Matchers {
  describe("StringConsumer") {
    describe("tryMatch") {
      it("should match the string") {
        val consumer = new StringConsumer("\"", "\\", "\n")
        val text = new LexingText("\"Some test\"")

        val test = consumer.tryMatch(text)
        assert(test.isDefined)
        val matched = test.get
        assert(matched.isInstanceOf[Hit])
        val hit = matched.asInstanceOf[Hit]
        assert(hit == Hit("STRING", 11, Some("Some test")))
      }

      it("should match the string and remove escape characters") {
        val consumer = new StringConsumer("\"", "\\", "\n")
        val text = new LexingText("\"Some \\\"test\\\"\"")

        val test = consumer.tryMatch(text)
        assert(test.isDefined)
        val matched = test.get
        assert(matched.isInstanceOf[Hit])
        val hit = matched.asInstanceOf[Hit]
        assert(hit == Hit("STRING", 15, Some("Some \"test\"")))
      }

      it("should fail if the string is divided by newlines") {
        val consumer = new StringConsumer("\"", "\\", "\n")
        val text = new LexingText("\"Some \\\"test\\\"\n\"")

        val exception = intercept[Unclosed] {
          consumer.tryMatch(text)
        }

        assert(exception == Unclosed("String", Position(0, 0)))
      }

      it("should fail if the string is not closed") {
        val consumer = new StringConsumer("\"", "\\", "\n")
        val text = new LexingText("\"Some \\\"test\\\"")

        val exception = intercept[Unclosed] {
          consumer.tryMatch(text)
        }

        assert(exception == Unclosed("String", Position(0, 0)))
      }
    }
  }
}
