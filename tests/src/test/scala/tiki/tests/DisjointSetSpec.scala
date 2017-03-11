package tiki
package tests


import tiki.Predef._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.Checkers
import org.scalatest.Matchers
import tiki.tests.arbitrary.AllArbitrary


/**
  * Test cases for the `DisjoitSet`.
  */
class DisjointSetSpec extends TikiSuite with Checkers with Matchers with AllArbitrary {


  /* Check empty sets, etc. */
  test("initial number of components equals size of the set")(forAll { (xs: Set[Int]) =>
    DisjointSet(xs).components should equal(xs.size)
  })


  

}
