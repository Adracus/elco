package de.adracus.elco.ast

import de.adracus.elco.lexer.core.Token

object Pimps {
  type S = String

  implicit class PimpedAny(val any: Any) {
    def as[A] = any.asInstanceOf[A]
  }

  implicit def e1toMethod[B1](e1: Extractor1[_, B1]): Seq[_] => B1 = e1.method
  implicit def e2toMethod[B1, B2](e2: Extractor2[_, _, B1, B2]): Seq[_] => (B1, B2) = e2.method
  implicit def e3toMethod[B1, B2, B3](e3: Extractor2[_, _, B1, B2]): Seq[_] => (B1, B2) = e3.method
  implicit def e4toMethod[B1, B2, B3, B4](e4: Extractor2[_, _, B1, B2]): Seq[_] => (B1, B2) = e4.method
  implicit def e5toMethod[B1, B2, B3, B4, B5](e5: Extractor2[_, _, B1, B2]): Seq[_] => (B1, B2) = e5.method
  implicit def e6toMethod[B1, B2, B3, B4, B5, B6](e6: Extractor2[_, _, B1, B2]): Seq[_] => (B1, B2) = e6.method
}

trait Extractor1[A1, B1] {
  def extract(subject: A1): B1

  def %[A2, B2](other: Extractor1[A2, B2]) = new Extractor2[A1, A2, B1, B2](this, other)

  def method = (seq: Seq[_]) => seq match {
    case Seq(a: A1) => extract(a)
  }
}

trait TokenExtractor[A] extends Extractor1[Token, A] {
  def extract(token: Token): A = {
    token.value.get.asInstanceOf[A]
  }
}

class A[A] {
  def apply[B](extractor: A => B) = new Extractor1[A, B] {
    override def extract(subject: A): B = extractor(subject)
  }

  def apply() = apply[A](identity[A])
}

object Ignore

object A {
  def apply[B] = new A[B]
}

object Text extends TokenExtractor[String]
object IntNumber extends TokenExtractor[Int]
object DoubleNumber extends TokenExtractor[Double]

object T {
  def apply[A] = new TokenExtractor[A] {}
}

case class Extractor2[A1, A2, B1, B2](t1: Extractor1[A1, B1], t2: Extractor1[A2, B2]) {
  def extract(a: A1, b: A2) = (t1.extract(a), t2.extract(b))

  def %[A3, B3](t3: Extractor1[A3, B3]) = Extractor3(t1, t2, t3)

  def method = (seq: Seq[_]) => seq match {
    case Seq(a: A1, b: A2) => extract(a, b)
  }
}

case class Extractor3[A1, A2, A3, B1, B2, B3](
    t1: Extractor1[A1, B1],
    t2: Extractor1[A2, B2],
    t3: Extractor1[A3, B3]) {
  def extract(a: A1, b: A2, c: A3) = (t1.extract(a), t2.extract(b), t3.extract(c))

  def %[A4, B4](t4: Extractor1[A4, B4]) = Extractor4(t1, t2, t3, t4)

  def method = (seq: Seq[_]) => seq match {
    case Seq(a: A1, b: A2, c: A3) => extract(a, b, c)
  }
}

case class Extractor4[A1, A2, A3, A4, B1, B2, B3, B4](
    t1: Extractor1[A1, B1],
    t2: Extractor1[A2, B2],
    t3: Extractor1[A3, B3],
    t4: Extractor1[A4, B4]) {
  def extract(a: A1, b: A2, c: A3, d: A4) =
    (t1.extract(a), t2.extract(b), t3.extract(c), t4.extract(d))

  def %[A5, B5](t5: Extractor1[A5, B5]) = Extractor5(t1, t2, t3, t4, t5)
}

case class Extractor5[A1, A2, A3, A4, A5, B1, B2, B3, B4, B5](
    t1: Extractor1[A1, B1],
    t2: Extractor1[A2, B2],
    t3: Extractor1[A3, B3],
    t4: Extractor1[A4, B4],
    t5: Extractor1[A5, B5]) {
  def extract(a: A1, b: A2, c: A3, d: A4, e: A5) =
    (t1.extract(a), t2.extract(b), t3.extract(c), t4.extract(d), t5.extract(e))

  def %[A6, B6](t6: Extractor1[A6, B6]) = Extractor6(t1, t2, t3, t4, t5, t6)
}

case class Extractor6[A1, A2, A3, A4, A5, A6, B1, B2, B3, B4, B5, B6](
    t1: Extractor1[A1, B1],
    t2: Extractor1[A2, B2],
    t3: Extractor1[A3, B3],
    t4: Extractor1[A4, B4],
    t5: Extractor1[A5, B5],
    t6: Extractor1[A6, B6]) {
  def extract(a: A1, b: A2, c: A3, d: A4, e: A5, f: A6) =
    (t1.extract(a), t2.extract(b), t3.extract(c), t4.extract(d), t5.extract(e), t6.extract(f))

  def %[A7, B7](t7: Extractor1[A7, B7]) = Extractor7(t1, t2, t3, t4, t5, t6, t7)
}

case class Extractor7[A1, A2, A3, A4, A5, A6, A7, B1, B2, B3, B4, B5, B6, B7](
    t1: Extractor1[A1, B1],
    t2: Extractor1[A2, B2],
    t3: Extractor1[A3, B3],
    t4: Extractor1[A4, B4],
    t5: Extractor1[A5, B5],
    t6: Extractor1[A6, B6],
    t7: Extractor1[A7, B7]) {

  def extract(a: A1, b: A2, c: A3, d: A4, e: A5, f: A6, g: A7) =
    (t1.extract(a), t2.extract(b), t3.extract(c), t4.extract(d), t5.extract(e), t6.extract(f), t7.extract(g))
}
