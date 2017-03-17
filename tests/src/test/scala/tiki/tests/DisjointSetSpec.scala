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

  test("union of all elements should yield a single component .2") {
    /* force path in union method. */
    val disjointSet = List((1,2),(3,1)).foldLeft(DisjointSet(Set(1,2,3)))((acc,v)=> {
      val union = acc.union(v._1, v._2)
      union.getOrElse(acc)
    })
    disjointSet.components should be (1)
  }

  test("find of an element not in the set should return None")(forAll { (xs: Set[Int], ys: Set[Int]) => {
    val d = xs.diff(ys)
    val disjointSet = DisjointSet(ys)
    if (d.nonEmpty) d.forall(disjointSet.find(_).isEmpty) should be (true)
  }})

  test("find of an element in the set should be defined")(forAll { (xs: Set[Int]) => {
    val disjointSet = DisjointSet(xs)
    xs.forall(disjointSet.find(_).isDefined) should be (true)
  }})

  test("union of elements in the same partition should return the same disjoint set") {
    val set = DisjointSet(Set(1,2)).union(1,2)
    set should be (set.getOrElse(DisjointSet.empty[Int]).union(1,2))
  }

  test("number of components of an empty set is zero") {
    DisjointSet(Set.empty[Int]).components should equal(0)
  }

  test("number of components of an DisjointSet.empty is zero") {
    DisjointSet.empty.components should equal(0)
  }

}
