package lexer

/**
 * Created by axel on 11/05/15.
 */
class RegexConsumer(val identifier: String,
                    val regex: String,
                    val transform: String => Any = (s: String) => s) extends Consumer {
  override def consume(lexingState: LexingState): Option[Token] = {
    val matched = lexingState.stepRegex(regex)
    if (matched.isEmpty) return None
    lexingState.makeToken(identifier, transform(matched.get))
  }
}
