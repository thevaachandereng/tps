package tps.util

object MathUtils {
  def roundTo(n: Double, digits: Int): Double = {
    (n * (Math.pow(10.0, digits))).toInt / Math.pow(10.0, digits)
  }

  def max(xs: Seq[Double]): Double = xs.reduceLeft(Math.max)
  def min(xs: Seq[Double]): Double = xs.reduceLeft(Math.min)

  def average(xs: Iterable[Double]): Double = {
    xs.sum / xs.size
  }

  def median(xs: Iterable[Double]): Double = {
    val sorted = xs.toIndexedSeq.sorted
    val i = sorted.size / 2
    if (sorted.size % 2 == 0) {
      (sorted(i - 1) + sorted(i)) / 2.0
    } else {
      sorted(i)
    }
  }

  def log2(x: Double) = scala.math.log(x) / scala.math.log(2)

  def combination[A](l: List[A], k: Int): List[List[A]] = {
    if (k == 0) {
      List(Nil)
    } else l match {
      case x :: xs => {
        val includeFirst = combination(xs, k - 1) map { x :: _ }
        val excludeFirst = combination(xs, k)
        includeFirst ::: excludeFirst
      }
      case Nil => {
        List()
      }
    }
  }

  def cartesianProduct[A, B](s1: Set[A], s2: Set[B]): Set[(A, B)] = {
   for (e1 <- s1; e2 <- s2) yield (e1, e2)
  }

  def cartesianProduct[A](ss: List[Set[A]]): Set[List[A]] = ss match {
    case s1 :: rest => {
      s1 flatMap { e => 
        cartesianProduct(rest) map { p => e :: p }
      }
    }
    case Nil => Set(Nil)
  }
}
