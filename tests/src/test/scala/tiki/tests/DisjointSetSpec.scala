package tiki
package tests


import tiki.Predef._
import org.scalatest.prop.Checkers
import org.scalatest.Matchers
import tiki.tests.arbitrary.AllArbitrary


/**
  * Test cases for the `DisjointSet`.
  */
class DisjointSetSpec extends TikiSuite with Checkers with Matchers with AllArbitrary {

  test("initial number of components equals size of the set")(forAll { (xs: Set[Int]) =>
    DisjointSet(xs).components should equal(xs.size)
  })

  test("union of all elements should yield a single component")(forAll { (xs: Set[Int]) => {
    val ys = xs.iterator.sliding(2)
    val disjointSet = ys.foldLeft(DisjointSet(xs))((acc, v) => acc.union(v.head, v.last).getOrElse(acc))
    if (xs.nonEmpty) disjointSet.components should equal(1) else disjointSet.components should equal(0)
  }})

  test("find of an element not in the set should return None")(forAll { (xs: Set[Int], ys: Set[Int]) => {
    val d = xs.diff(ys)
    val disjointSet = DisjointSet(ys)
    if (d.nonEmpty) d.forall(disjointSet.find(_).isEmpty) should be (true)
  }})

  test("find of an element in the set should be defined")(forAll { (xs: Set[Int]) => {
    val disjointSet = DisjointSet(xs)
    xs.forall(disjointSet.find(_).isDefined) should be (true)
  }})

  test("number of components of an empty set is zero") {
    DisjointSet(Set.empty[Int]).components should equal(0)
  }

  test("number of components of an DisjointSet.empty is zero") {
    DisjointSet.empty.components should equal(0)
  }

}
