import de.adracus.elco.grammar.core._
import de.adracus.elco.parser.{Item, ItemSet, Parser}
import org.scalatest.{Matchers, FunSpec}

/**
 * Created by axel on 01/06/15.
 */
class ParserSpec extends FunSpec with Matchers {
  def t(value: String) = Terminal(value)

  object TestGrammar extends Grammar {
    'E := 'T & 'Et

    'Et := "+" & 'T & 'Et | Epsilon

    'T := 'F & 'Tt

    'Tt := "*" & 'F & 'Tt | Epsilon

    'F := "(" & 'E & ")"

    'F := "id"
  }

  object ItemSetGrammar extends Grammar {
    'S := 'N
    'N := 'V & "=" & 'E | 'E
    'E := 'V
    'V := "x" | "*" & 'E
  }

  val parser = new Parser(TestGrammar)
  var itemParser = new Parser(ItemSetGrammar)

  println(itemParser.computeExtendedGrammar(ItemSetGrammar))

  describe("Grammar") {
    describe("first") {
      it("should correctly calculate the first set") {
        assert(parser.first == Map(
          "E" -> Set[Statement](t("("), t("id")),
          "T" -> Set[Statement](t("("), t("id")),
          "F" -> Set[Statement](t("("), t("id")),
          "Tt" -> Set[Statement](t("*"), Epsilon),
          "Et" -> Set[Statement](t("+"), Epsilon)))
      }
    }

    describe("follow") {
      it("should correctly calculate the follow set") {
        assert(parser.follow == Map(
          "E" -> Set(End, t(")")),
          "T" -> Set(t("+"), End, t(")")),
          "F" -> Set(t("*"), t("+"), End, t(")")),
          "Tt" -> Set(t("+"), End, t(")")),
          "Et" -> Set(End, t(")"))))
      }
    }

    describe("itemSets") {
      it("should correctly calculate the item sets") {
        implicit def stringToTerminal(string: String): Terminal = Terminal(string)
        implicit def symbolToNonTerminal(symbol: Symbol): NonTerminal = NonTerminal(symbol.name)

        def item(nonTerminal: String, marker: Int, statements: Statement*) = {
          Item(marker, Rule(NonTerminal(nonTerminal), Production(statements:_*)))
        }

        def itemSet(item: Item*) = ItemSet(item.toSet)

        assert(itemParser.itemSets == Set(
          itemSet(
            item("S", 0, 'N),
            item("N", 0, 'V, "=", 'E),
            item("N", 0, 'E),
            item("E", 0, 'V),
            item("V", 0, "x"),
            item("V", 0, "*", 'E)
          ),
          itemSet(
            item("V", 1, "x")
          ),
          itemSet(
            item("V", 1, "*", 'E),
            item("E", 0, 'V),
            item("V", 0, "x"),
            item("V", 0, "*", 'E)
          ),
          itemSet(
            item("N", 1, 'V, "=", 'E),
            item("E", 1, 'V)
          ),
          itemSet(
            item("S", 1, 'N)
          ),
          itemSet(
            item("N", 1, 'E)
          ),
          itemSet(
            item("V", 2, "*", 'E)
          ),
          itemSet(
            item("E", 1, 'V)
          ),
          itemSet(
            item("N", 2, 'V, "=", 'E),
            item("E", 0, 'V),
            item("V", 0, "x"),
            item("V", 0, "*", 'E)
          ),
          itemSet(
            item("N", 3, 'V, "=", 'E)
          )
        ))
      }
    }
  }
}
