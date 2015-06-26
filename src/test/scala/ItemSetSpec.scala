import de.adracus.elco.grammar.{Word, NonTerminal, GrammarBuilder}
import de.adracus.elco.grammar.core._
import de.adracus.elco.parser.Parser
import de.adracus.elco.production.ElcoLexer
import org.scalatest.{FunSpec, Matchers}

/**
 * Created by axel on 09/06/15.
 */
class ItemSetSpec extends FunSpec with Matchers {
  object TestGrammarBuilder extends GrammarBuilder {
    'L := 'E & 'L | 'E
    'E := 'E & "+" & 'E | 'Number
    'Number := "INTEGER" | "DOUBLE"
  }

  val grammar = TestGrammarBuilder.build()

  def n(name: String) = NonTerminal(name)
  def t(name: String) = Word(name)

  implicit def stringToWord(string: String): Word = Word(string)
  implicit def symbolToNonTerminal(symbol: Symbol): NonTerminal = NonTerminal(symbol.name)

  describe("ItemSet") {
    describe("from") {
      /*it("should correctly compute the item set") {
        val set = ItemSet.from(grammar.startRule, Set() ++ grammar.rules)

        assert(set == ItemSet(Set(
          Item.start(Rule(n("Start"), Production(List(n("L"))))),
          Item.start(Rule(n("L"), Production(List(n("E"), n("L"))))),
          Item.start(Rule(n("E"), Production(List(n("E"), t("+"), n("E"))))),
          Item.start(Rule(n("E"), Production(List(n("Number"))))),
          Item.start(Rule(n("Number"), Production(List(t("INTEGER"))))),
          Item.start(Rule(n("Number"), Production(List(t("DOUBLE")))))
        )))
      }*/
    }
    
    describe("all") {
      it("should compute all item sets for the given grammar") {
        val parser = Parser parsing grammar
        val lexer = new ElcoLexer
        val stream = lexer.lex("1 + 2")

        val tree = parser.parse(stream)
        println(tree)
      }
    }
  }
}
