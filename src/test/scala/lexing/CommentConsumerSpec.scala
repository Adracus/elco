package lexing

import lexer.{LexingState, CommentConsumer, Position, UnclosedException}
import org.scalatest._

/**
 * Created by axel on 08/05/15.
 */
class CommentConsumerSpec extends FunSpec with Matchers {
  val consumer = new CommentConsumer()

  describe("A CommentConsumer") {
    it("should not process if there is no comment symbol") {
      val state = new LexingState("Some text without comments")
      consumer consume state
      assert(state.current == "S")
      assert(state.position.column == 0)
      assert(state.position.line == 0)
    }

    describe("eating a comment") {
      it("should eat a comment without new lines") {
        val state = new LexingState("/* Some comment */Here text")
        consumer consume state
        assert(state.current == "H")
        assert(state.position.column == 18)
      }

      it("should eat a comment with new lines") {
        val state = new LexingState("/* Some comment \n*/Here text")
        consumer consume state
        assert(state.current == "H")
        assert(state.position.column == 2)
        assert(state.position.line == 1)
      }

      it("should throw an exception if there is an unclosed comment") {
        val state = new LexingState("/* Some unclosed comment")
        assert(intercept[UnclosedException] {
          consumer consume state
        }.start == Position(0, 0))
      }
    }
  }
}
