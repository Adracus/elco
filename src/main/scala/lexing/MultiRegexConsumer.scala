package lexing

import scala.util.control.NonFatal

/**
 * Created by axel on 11/05/15.
 */
class MultiRegexConsumer(val consumers: RegexConsumer*) extends Consumer {
  override def consume(lexingState: LexingState): Option[Token] = {
    try {
      val matchPair = consumers
        .map((r: RegexConsumer) => (r, lexingState.matchRegex(r.regex)))
        .filter(_._2.isDefined)
        .maxBy(_._2.get.length)
      matchPair._1.consume(lexingState)
    } catch {
      case NonFatal(exc) => None
    }
  }

  def or(consumer: RegexConsumer) = {
    new MultiRegexConsumer(consumers.+:(consumer):_*)
  }
}

object MultiRegexConsumer {
  implicit class RichRegexConsumer(val consumer: RegexConsumer) {
    def or(regexConsumer: RegexConsumer) = {
      new MultiRegexConsumer(consumer, regexConsumer)
    }
  }
}
