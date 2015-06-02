import de.adracus.elco.grammar.core._
import org.scalatest.{Matchers, FunSpec}

/**
 * Created by axel on 01/06/15.
 */
class GrammarSpec extends FunSpec with Matchers {
  def t(value: String) = Terminal(value)

  object TestGrammar extends Grammar {
    'E := 'T & 'Et

    'Et := "+" & 'T & 'Et | Epsilon

    'T := 'F & 'Tt

    'Tt := "*" & 'F & 'Tt | Epsilon

    'F := "(" & 'E & ")"

    'F := "id"
  }

  describe("Grammar") {
    describe("first") {
      it("should correctly calculate the first set") {
        assert(TestGrammar.first == Map(
          "E" -> Set[Statement](t("("), t("id")),
          "T" -> Set[Statement](t("("), t("id")),
          "F" -> Set[Statement](t("("), t("id")),
          "Tt" -> Set[Statement](t("*"), Epsilon),
          "Et" -> Set[Statement](t("+"), Epsilon)))
      }
    }

    describe("follow") {
      it("should correctly calculate the follow set") {
        assert(TestGrammar.follow == Map(
          "E" -> Set(End, t(")")),
          "T" -> Set(t("+"), End, t(")")),
          "F" -> Set(t("*"), t("+"), End, t(")")),
          "Tt" -> Set(t("+"), End, t(")")),
          "Et" -> Set(End, t(")"))))
      }
    }
  }
}
