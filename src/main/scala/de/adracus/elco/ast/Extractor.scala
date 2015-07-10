package de.adracus.elco.ast

import de.adracus.elco.lexer.core.Token

object Extractor {
  type E1[B1] = Seq[_] => B1
  type E2[B1, B2] = Seq[_] => (B1, B2)
  type E3[B1, B2, B3] = Seq[_] => (B1, B2, B3)
  type E4[B1, B2, B3, B4] = Seq[_] => (B1, B2, B3, B4)
  type E5[B1, B2, B3, B4, B5] = Seq[_] => (B1, B2, B3, B4, B5)
  type E6[B1, B2, B3, B4, B5, B6] = Seq[_] => (B1, B2, B3, B4, B5, B6)

  implicit def ex1ToE1[_, B1](ex1: Extractor1[_, B1]): E1[B1] = ex1.method
  implicit def ex2ToE2[_, _, B1, B2](ex2: Extractor2[_, _, B1, B2]): E2[B1, B2] = ex2.method
  implicit def ex3ToE3[_, _, _, B1, B2, B3](ex3: Extractor3[_, _, _, B1, B2, B3]): E3[B1, B2, B3] = ex3.method
  implicit def ex4ToE4[_, _, _, _, B1, B2, B3, B4]
    (ex4: Extractor4[_, _, _, _, B1, B2, B3, B4]): E4[B1, B2, B3, B4] = ex4.method
  implicit def ex5ToE5[_, _, _, _, _, B1, B2, B3, B4, B5]
    (ex5: Extractor5[_, _, _, _, _, B1, B2, B3, B4, B5]): E5[B1, B2, B3, B4, B5] = ex5.method
  implicit def ex6ToE6[_, _, _, _, _, _, B1, B2, B3, B4, B5, B6]
    (ex6: Extractor6[_, _, _, _, _, _, B1, B2, B3, B4, B5, B6]): E6[B1, B2, B3, B4, B5, B6] = ex6.method
}

object Ignore

trait Extractor1[A1, B1] {
  def apply(seq: Seq[_]) = seq match {
    case Seq(a: A1, rest @ _ *) => (extract(a), rest)
  }

  def %(ignore: Ignore.type) = IgnExtractor1(this)
  def %[A2, B2](e: Extractor1[A2, B2]) = Extractor2[A1, A2, B1, B2](this, e)
  def pipe(seq: Seq[_]) = apply(seq)._1

  def extract(a: A1): B1
  def method = (seq: Seq[_]) => pipe(seq)
}

trait TokenExtractor[A] extends Extractor1[Token, A] {
  def extract(token: Token) = token.value.get.asInstanceOf[A]
}

object IntNumber extends TokenExtractor[Int]

case class IgnExtractor1[A1, B1](e1: Extractor1[A1, B1]) extends Extractor1[A1, B1] {
  override def apply(seq: Seq[_]) = {
    val (b1, rest) = e1.apply(seq)
    (b1, rest.tail)
  }

  def extract(a: A1) = e1.extract(a)
}

trait BaseExtractor2[A1, A2, B1, B2] {
  def apply(seq: Seq[_]): ((B1, B2), Seq[_])

  def extract(a1: A1, a2: A2): (B1, B2)

  def %(ignore: Ignore.type) = IgnExtractor2(this)
  def %[A3, B3](e1: Extractor1[A3, B3]) = Extractor3(this, e1)
  def pipe(seq: Seq[_]) = apply(seq)._1

  def method = (seq: Seq[_]) => pipe(seq)
}

case class Extractor2[A1, A2, B1, B2](e1: Extractor1[A1, B1], e2: Extractor1[A2, B2])
    extends BaseExtractor2[A1, A2, B1, B2]{
  def apply(seq: Seq[_]) = {
    val (b1, firstRest) = e1.apply(seq)
    val (b2, rest) = e2.apply(firstRest)
    ((b1, b2), rest)
  }

  def extract(a: A1, b: A2) = (e1.extract(a), e2.extract(b))
}

case class IgnExtractor2[A1, A2, B1, B2](e2: BaseExtractor2[A1, A2, B1, B2]) extends BaseExtractor2[A1, A2, B1, B2] {
  def apply(seq: Seq[_]) = {
    val (b1b2, rest) = e2.apply(seq)
    (b1b2, rest.tail)
  }

  def extract(a1: A1, a2: A2) = e2.extract(a1, a2)
}

trait BaseExtractor3[A1, A2, A3, B1, B2, B3] {
  def apply(seq: Seq[_]): ((B1, B2, B3), Seq[_])

  def extract(a1: A1, a2: A2, a3: A3): (B1, B2, B3)

  def ~(ignore: Ignore.type) = IgnExtractor3(this)
  def n[A4, B4](e1: Extractor1[A4, B4]) = Extractor4(this, e1)
  def pipe(seq: Seq[_]) = apply(seq)._1

  def method = (seq: Seq[_]) => pipe(seq)
}

case class Extractor3[A1, A2, A3, B1, B2, B3](e2: BaseExtractor2[A1, A2, B1, B2], e1: Extractor1[A3, B3])
  extends BaseExtractor3[A1, A2, A3, B1, B2, B3] {
  def apply(seq: Seq[_]) = {
    val ((b1, b2), firstRest) = e2.apply(seq)
    val (b3, rest) = e1.apply(firstRest)
    ((b1, b2, b3), rest)
  }

  def extract(a: A1, b: A2, c: A3) = {
    val (b1, b2) = e2.extract(a, b)
    (b1, b2, e1.extract(c))
  }
}

case class IgnExtractor3[A1, A2, A3, B1, B2, B3](e3: BaseExtractor3[A1, A2, A3, B1, B2, B3])
  extends BaseExtractor3[A1, A2, A3, B1, B2, B3] {
  def apply(seq: Seq[_]) = {
    val (b1b2b3, rest) = e3.apply(seq)
    (b1b2b3, rest.tail)
  }

  def extract(a1: A1, a2: A2, a3: A3) = e3.extract(a1, a2, a3)
}

trait BaseExtractor4[A1, A2, A3, A4, B1, B2, B3, B4] {
  def apply(seq: Seq[_]): ((B1, B2, B3, B4), Seq[_])

  def extract(a1: A1, a2: A2, a3: A3, a4: A4): (B1, B2, B3, B4)

  def %(ignore: Ignore.type) = IgnExtractor4(this)
  def %[A5, B5](e1: Extractor1[A5, B5]) = Extractor5(this, e1)
  def pipe(seq: Seq[_]) = apply(seq)._1

  def method = (seq: Seq[_]) => pipe(seq)
}

case class Extractor4[A1, A2, A3, A4, B1, B2, B3, B4](e3: BaseExtractor3[A1, A2, A3, B1, B2, B3], e1: Extractor1[A4, B4])
  extends BaseExtractor4[A1, A2, A3, A4, B1, B2, B3, B4] {
  def apply(seq: Seq[_]) = {
    val ((b1, b2, b3), firstRest) = e3.apply(seq)
    val (b4, rest) = e1.apply(firstRest)
    ((b1, b2, b3, b4), rest)
  }

  def extract(a: A1, b: A2, c: A3, d: A4) = {
    val (b1, b2, b3) = e3.extract(a, b, c)
    (b1, b2, b3, e1.extract(d))
  }
}

case class IgnExtractor4[A1, A2, A3, A4, B1, B2, B3, B4](e4: BaseExtractor4[A1, A2, A3, A4, B1, B2, B3, B4])
  extends BaseExtractor4[A1, A2, A3, A4, B1, B2, B3, B4] {
  def apply(seq: Seq[_]) = {
    val (b1b2b3b4, rest) = e4.apply(seq)
    (b1b2b3b4, rest.tail)
  }

  def extract(a1: A1, a2: A2, a3: A3, a4: A4) = e4.extract(a1, a2, a3, a4)
}

trait BaseExtractor5[A1, A2, A3, A4, A5, B1, B2, B3, B4, B5] {
  def apply(seq: Seq[_]): ((B1, B2, B3, B4, B5), Seq[_])

  def extract(a1: A1, a2: A2, a3: A3, a4: A4, a5: A5): (B1, B2, B3, B4, B5)

  def %(ignore: Ignore.type) = IgnExtractor5(this)
  def %[A6, B6](e1: Extractor1[A6, B6]) = Extractor6(this, e1)
  def pipe(seq: Seq[_]) = apply(seq)._1

  def method = (seq: Seq[_]) => pipe(seq)
}

case class Extractor5[A1, A2, A3, A4, A5, B1, B2, B3, B4, B5](e4: BaseExtractor4[A1, A2, A3, A4, B1, B2, B3, B4], e1: Extractor1[A5, B5])
  extends BaseExtractor5[A1, A2, A3, A4, A5, B1, B2, B3, B4, B5] {
  def apply(seq: Seq[_]) = {
    val ((b1, b2, b3, b4), firstRest) = e4.apply(seq)
    val (b5, rest) = e1.apply(firstRest)
    ((b1, b2, b3, b4, b5), rest)
  }

  def extract(a1: A1, a2: A2, a3: A3, a4: A4, a5: A5) = {
    val (b1, b2, b3, b4) = e4.extract(a1, a2, a3, a4)
    (b1, b2, b3, b4, e1.extract(a5))
  }
}

case class IgnExtractor5[A1, A2, A3, A4, A5, B1, B2, B3, B4, B5](e5: BaseExtractor5[A1, A2, A3, A4, A5, B1, B2, B3, B4, B5])
  extends BaseExtractor5[A1, A2, A3, A4, A5, B1, B2, B3, B4, B5] {
  def apply(seq: Seq[_]) = {
    val (b1b2b3b4b5, rest) = e5.apply(seq)
    (b1b2b3b4b5, rest.tail)
  }

  def extract(a1: A1, a2: A2, a3: A3, a4: A4, a5: A5) = e5.extract(a1, a2, a3, a4, a5)
}

trait BaseExtractor6[A1, A2, A3, A4, A5, A6, B1, B2, B3, B4, B5, B6] {
  def apply(seq: Seq[_]): ((B1, B2, B3, B4, B5, B6), Seq[_])

  def extract(a1: A1, a2: A2, a3: A3, a4: A4, a5: A5, a6: A6): (B1, B2, B3, B4, B5, B6)

  def %(ignore: Ignore.type) = IgnExtractor6(this)
  def pipe(seq: Seq[_]) = apply(seq)._1

  def method = (seq: Seq[_]) => pipe(seq)
}

case class Extractor6[A1, A2, A3, A4, A5, A6, B1, B2, B3, B4, B5, B6](e5: BaseExtractor5[A1, A2, A3, A4, A5, B1, B2, B3, B4, B5], e1: Extractor1[A6, B6])
  extends BaseExtractor6[A1, A2, A3, A4, A5, A6, B1, B2, B3, B4, B5, B6] {
  def apply(seq: Seq[_]) = {
    val ((b1, b2, b3, b4, b5), firstRest) = e5.apply(seq)
    val (b6, rest) = e1.apply(firstRest)
    ((b1, b2, b3, b4, b5, b6), rest)
  }

  def extract(a1: A1, a2: A2, a3: A3, a4: A4, a5: A5, a6: A6) = {
    val (b1, b2, b3, b4, b5) = e5.extract(a1, a2, a3, a4, a5)
    (b1, b2, b3, b4, b5, e1.extract(a6))
  }
}

case class IgnExtractor6[A1, A2, A3, A4, A5, A6, B1, B2, B3, B4, B5, B6](e6: BaseExtractor6[A1, A2, A3, A4, A5, A6, B1, B2, B3, B4, B5, B6])
  extends BaseExtractor6[A1, A2, A3, A4, A5, A6, B1, B2, B3, B4, B5, B6] {
  def apply(seq: Seq[_]) = {
    val (b1b2b3b4b5b6, rest) = e6.apply(seq)
    (b1b2b3b4b5b6, rest.tail)
  }

  def extract(a1: A1, a2: A2, a3: A3, a4: A4, a5: A5, a6: A6) = e6.extract(a1, a2, a3, a4, a5, a6)
}

object Test {
  IntNumber % IntNumber % IntNumber % IntNumber % IntNumber % IntNumber % Ignore
}