package tiki
package tests

import tiki.tests.arbitrary.AllArbitrary
import tiki.Predef._

class WeightedDigraphSpec extends TikiSuite with AllArbitrary {

  test("Empty digraph should have no edges") {
    val empty = WeightedDigraph.empty[Int]
    empty.edges should be(Stream.empty)
    empty.vertices should be (Stream.empty)
  }

  test("empty digraph should return no successors or predecessors")(forAll { (v: Int) =>
    val empty = WeightedDigraph.empty[Int]
    empty.contains(v) should be (false)
    empty.successors(v) should be (Set.empty)
    empty.predecessors(v) should be (Set.empty)
  })

}
