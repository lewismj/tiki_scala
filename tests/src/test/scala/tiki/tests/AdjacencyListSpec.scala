package tiki
package tests



import org.scalatest.Matchers
import org.scalatest.prop.Checkers
import tiki.tests.arbitrary.AllArbitrary
import tiki.Predef._

class AdjacencyListSpec extends TikiSuite with Checkers with Matchers with AllArbitrary {

  test("`children` of adjacency list should return correct vertices")(forAll { (xs: List[EdgeLike[Int]]) =>
    /* doesn't test leaf vertices. */
    val adjacencyList = AdjacencyList(xs)
    xs.groupBy(_.from).map { case (k,v) => (k, v.map(_.to))}.forall { case (vertex, children) =>
      adjacencyList.children(vertex) == Some(children.toSet)
    } should be (true)
  })

  test("`children` of leaf vertices should be the empty set.") { (xs: List[EdgeLike[Int]]) =>
    val adjacencyList = AdjacencyList(xs)
    xs.filter(x => ! xs.exists(_.from == x.to )).forall(e=>adjacencyList.children(e.to) == Some(Set.empty[Int]))
  }

  test("`children` of vertex not in graph should return none") {
    AdjacencyList.empty[Int].children(1) should be (None)
  }

  test("`parents` of vertex not in graph should return none") {
    AdjacencyList.empty[Int].parents(1) should be (None)
  }

  test("`parents` of adjacency list should return correct vertices")(forAll { (xs: List[EdgeLike[Int]]) =>
    /* doesn't test leaf vertices. */
    val adjacencyList = AdjacencyList(xs)
    xs.groupBy(_.to).map { case (k,v) => (k, v.map(_.from))}.forall { case (vertex, parents) =>
      adjacencyList.parents(vertex) == Some(parents.toSet)
    } should be (true)
  })

  test("`parents` of root vertices should be the empty set.") { (xs: List[EdgeLike[Int]]) =>
    val adjacencyList = AdjacencyList(xs)
    xs.filter(x => ! xs.exists(_.to == x.from )).forall(e=>adjacencyList.children(e.to) == Some(Set.empty[Int]))
  }

}
