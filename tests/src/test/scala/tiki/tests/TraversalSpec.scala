package tiki
package tests

import tiki.Traversal._
import tiki.implicits._
import tiki.tests.arbitrary.AllArbitrary
import scala.util.Random


class TraversalSpec extends TikiSuite with AllArbitrary {

  test("Function `dfs` single node with edge to self.") {
    val edges = Stream('A' --> 'A')
    val adj = AdjacencyList(edges)
    val search = dfs(adj, 'A')
    search should be(Seq('A'))
  }

  test("Function `bfs` single node with edge to self.") {
    val edges = Random.shuffle(List('A' --> 'A')).toStream
    val adj = AdjacencyList(edges)
    val search = bfs(adj, 'A')
    search should be(Seq('A'))
  }

  test("Function `dfs` on cycle returns visited node once.") {
    val edges = Random.shuffle(List('A' --> 'B', 'B' --> 'C', 'C' --> 'A')).toStream
    val adj = AdjacencyList(edges)
    val search = dfs(adj, 'A')
    search should be(Seq('A','B','C'))
  }

  test("Function `bfs` on cycle returns visited node once.") {
    val edges = Random.shuffle(List('A' --> 'B', 'B' --> 'C', 'C' --> 'A')).toStream
    val adj = AdjacencyList(edges)
    val search = bfs(adj, 'A')
    search should be(Seq('A','B','C'))
  }

  test("Function `dfs` on missing vertex returns empty sequence.") {
    val edges = Random.shuffle(List('A' --> 'B', 'A' --> 'C', 'B' --> 'D', 'C' --> 'D')).toStream
    val adj = AdjacencyList(edges)
    val search = dfs(adj, 'E')
    search should be(Seq.empty[Char])
  }

  test("Function `dfs` returns correct ordering for simple graph.") {
    val edges = Random.shuffle(List('A' --> 'B', 'A' --> 'C', 'B' --> 'D', 'C' --> 'D')).toStream
    val adj = AdjacencyList(edges)
    val search = dfs(adj, 'A')
    val expected = Set(Seq('A', 'B', 'D', 'C'), Seq('A', 'C', 'D', 'B'))
    expected.contains(search) should be(true)
  }

  test("Function `bfs` returns correct ordering for simple graph.") {
    val edges = Random.shuffle(List('A' --> 'B', 'A' --> 'C', 'B' --> 'D', 'C' --> 'D')).toStream
    val adj = AdjacencyList(edges)
    val search = bfs(adj, 'A')
    val expected = Set(Seq('A', 'C', 'B', 'D'), Seq('A', 'B', 'C', 'D'))
    expected.contains(search) should be(true)
  }


}
