package lexing

import org.scalatest.{Matchers, FunSpec}

/**
 * Created by axel on 08/05/15.
 */
class StringConsumerSpec extends FunSpec with Matchers {
  val consumer = new StringConsumer()

  describe("A StringConsumer") {
    describe("when a string's present") {
      it("should consume it") {
        val state = new LexingState("\"Some text\"")
        val token = consumer consume state
        assert(token.isDefined)
        assert(token.get.value.get == "Some text")
      }

      it("should throw if the string's not closed") {
        val state = new LexingState("\"Some text")
        assert(intercept[UnclosedException] {
          consumer consume state
        }.start == Position(0, 0))
      }

      it("should consume the escaped variant") {
        val state = new LexingState("\"Some \\\"Text\\\"\"")
        val token = consumer consume state
        assert(token.isDefined)
        assert(token.get.value.get == "Some \"Text\"")
      }
    }
  }
}
