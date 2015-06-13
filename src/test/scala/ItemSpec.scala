import de.adracus.elco.grammar.core.{Word, Production, NonTerminal, Rule}
import de.adracus.elco.parser.Item
import org.scalatest.{Matchers, FunSpec}

/**
 * Created by axel on 13/06/15.
 */
class ItemSpec extends FunSpec with Matchers {
  val rule = Rule(NonTerminal("A"), Production(List(NonTerminal("B"), Word("C"), Word("D"))))

  describe("Item") {
    describe("before") {
      it("should return the values before the marker") {
        val item = Item(1, rule)

        assert(item.before == List(NonTerminal("B")))
      }

      it("should return the empty list if the marker is before the start of the rule") {
        val item = Item(0, rule)

        assert(item.before == Nil)
      }
    }

    describe("after") {
      it("should return the values after the marker") {
        val item = Item(2, rule)

        assert(item.after == List(Word("D")))
      }

      it("should return the empty list if the marker is at the end of the rule") {
        val item = Item(3, rule)

        assert(item.after == Nil)
      }
    }
  }
}
