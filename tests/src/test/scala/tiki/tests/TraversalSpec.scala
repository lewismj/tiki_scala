package tiki.tests

import org.scalatest.Matchers
import org.scalatest.prop.Checkers
import tiki.tests.arbitrary.AllArbitrary

import tiki.Predef._
import tiki.Traversal._
import tiki.implicits._

import scala.util.Random



class TraversalSpec extends TikiSuite with Checkers with Matchers with AllArbitrary {

  test("`dfs` on missing vertex returns empty sequence") {
    val edges = Random.shuffle(List('A' --> 'B', 'A' --> 'C', 'B' --> 'D', 'C' --> 'D'))
    val adj = buildAdjacencyList(edges)
    val search = dfs(adj, 'E')
    search should be(Seq.empty[Char])
  }

  test("`dfs` returns correct ordering for simple graph") {
    val edges = Random.shuffle(List('A' --> 'B', 'A' --> 'C', 'B' --> 'D', 'C' --> 'D'))
    val adj = buildAdjacencyList(edges)
    val search = dfs(adj, 'A')
    val expected = Set(Seq('A', 'B', 'D', 'C'), Seq('A', 'C', 'D', 'B'))
    expected.contains(search) should be(true)
  }

  test("`bfs` returns correct ordering for simple graph") {
    val edges = Random.shuffle(List('A' --> 'B', 'A' --> 'C', 'B' --> 'D', 'C' --> 'D'))
    val adj = buildAdjacencyList(edges)
    val search = bfs(adj, 'A')
    val expected = Set(Seq('A', 'C', 'B', 'D'), Seq('A', 'B', 'C', 'D'))
    expected.contains(search) should be(true)
  }

}
