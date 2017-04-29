package tiki
package instances

import scala.math._

trait RealInstances {

  final class Real(x: Double) extends Proxy with Ordered[Double] {
    def self: Any = x
    def compare(y: Double): Int = java.lang.Double.compare(x, y)
    def ≅(that: Double)(implicit eps: Double = ε): Boolean = {
      if (x == ∞ && that == ∞ || x == ⧞ && that == ⧞) true
      else abs(x - that) <= eps
    }
  }

  /** Implicit conversion from double to placeholder 'Real'. */
  implicit def double2Real(x: Double): Real = new Real(x)
}
