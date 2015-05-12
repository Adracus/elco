package lexing

import lexer.{LexingState, Position}
import org.scalatest.{Matchers, FunSpec}

/**
 * Created by axel on 08/05/15.
 */
class LexingStateSpec extends FunSpec with Matchers {
  describe("A LexingState") {
    describe("pointsAt") {
      it("should return true if it points at the desired characters") {
        val state = new LexingState("Wow some state")
        assert(state.pointsAt("Wow"))
      }

      it("should return false if it does not point at the desired characters") {
        val state = new LexingState("Wow some state")
        assert(!state.pointsAt("some"))
      }

      it("should return false if the characters overlap the text") {
        val state = new LexingState("Short text")
        assert(!state.pointsAt("Short text here?"))
      }
    }

    describe("regexPointsAt") {
      it("should return true if it starts with the specified regex") {
        val state = new LexingState("12345 woha")
        assert(state.regexPointsAt("[1-5]{5}"))
      }

      it("should return false if it does not match") {
        val state = new LexingState("woha 12345")
        assert(!state.regexPointsAt("[1-5]{5}"))
      }
    }

    describe("stepColumn") {
      it("should step one step and update the position") {
        val state = new LexingState("Test")
        assert(state.current == "T")
        state.stepColumn()
        assert(state.current == "e")
        assert(state.position == Position(1, 0))
      }

      it("should step multiple steps and update the position") {
        val state = new LexingState("Test")
        assert(state.current == "T")
        state.stepColumn(3)
        assert(state.current == "t")
        assert(state.position == Position(3, 0))
      }

      it("should step over the last character") {
        val state = new LexingState("Test")
        state.stepColumn(4)
      }
    }

    describe("stepRegex") {
      it("should step over the matched section") {
        val state = new LexingState("12345")
        assert(state.stepRegex("[1-5]+").get == "12345")
        assert(state.position == Position(5, 0))
      }
    }

    describe("stepUntil") {
      it("should return the word before the character") {
        val state = new LexingState("Test* more")
        assert(state.stepUntil(Set("*")) == "Test")
      }
    }

    describe("stepUntilEscaped") {
      it("should return the word and escape the character") {
        val state = new LexingState("Test#* more* and more")
        assert(state.stepUntilEscaped(Set("*"), Set("#")) == "Test* more")
      }
    }

    describe("current") {
      it("should return the current character") {
        val state = new LexingState("Test state")
        assert(state.current == "T")
        state.stepColumn()
        assert(state.current == "e")
      }
    }

    describe("charsLeft") {
      it("should return true if chars are left") {
        val state = new LexingState("Test")
        assert(state.charsLeft)
      }

      it("should return true if we are on the last character") {
        val state = new LexingState("Test")
        state.stepColumn(3)
        assert(state.charsLeft)
      }

      it("should return false if no chars are left") {
        val state = new LexingState("Test")
        state.stepColumn(4)
        assert(!state.charsLeft)
      }
    }
  }
}
