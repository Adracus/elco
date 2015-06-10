import de.adracus.elco.grammar.core._
import de.adracus.elco.parser.{TransitionTable, Item, ItemSet}
import org.scalatest.{Matchers, FunSpec}

/**
 * Created by axel on 09/06/15.
 */
class ItemSetSpec extends FunSpec with Matchers {
  object TestGrammarBuilder extends GrammarBuilder {
    'L := 'E & 'L
    'E := 'E & "+" & 'E | 'Number
    'Number := "INTEGER" | "DOUBLE"
  }

  val grammar = TestGrammarBuilder.build()

  def n(name: String) = NonTerminal(name)
  def t(name: String) = Word(name)

  describe("ItemSet") {
    describe("from") {
      it("should correctly compute the item set") {
        val set = ItemSet.from(grammar.startRule, Set() ++ grammar.rules)

        assert(set == ItemSet(Set(
          Item.start(Rule(n("Start"), Production(Seq(n("L"))))),
          Item.start(Rule(n("L"), Production(Seq(n("E"), n("L"))))),
          Item.start(Rule(n("E"), Production(Seq(n("E"), t("+"), n("E"))))),
          Item.start(Rule(n("E"), Production(Seq(n("Number"))))),
          Item.start(Rule(n("Number"), Production(Seq(t("INTEGER"))))),
          Item.start(Rule(n("Number"), Production(Seq(t("DOUBLE")))))
        )))
      }
    }
    
    describe("all") {
      it("should compute all item sets for the given grammar") {
        val sets = ItemSet.all(grammar)
        val table = new TransitionTable(sets, grammar.rules)
        println(table.transitions)
      }
    }
  }
}
