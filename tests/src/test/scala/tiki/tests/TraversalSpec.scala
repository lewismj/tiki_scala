package tiki
package tests

import tiki.Predef._
import tiki.Traversal._
import tiki.implicits._
import tiki.tests.arbitrary.AllArbitrary
import scala.util.Random


class TraversalSpec extends TikiSuite with AllArbitrary {

  test("`dfs` single node with edge to self") {
    val edges = Stream('A' --> 'A')
    val adj = AdjacencyList(edges)
    val search = dfs(adj, 'A')
    search should be(Seq('A'))
  }

  test("`bfs` single node with edge to self") {
    val edges = Random.shuffle(List('A' --> 'A')).toStream
    val adj = AdjacencyList(edges)
    val search = bfs(adj, 'A')
    search should be(Seq('A'))
  }

  test("`dfs` on cycle returns visited node once.") {
    val edges = Random.shuffle(List('A' --> 'B', 'B' --> 'C', 'C' --> 'A')).toStream
    val adj = AdjacencyList(edges)
    val search = dfs(adj, 'A')
    search should be(Seq('A','B','C'))
  }

  test("`bfs` on cycle returns visited node once.") {
    val edges = Random.shuffle(List('A' --> 'B', 'B' --> 'C', 'C' --> 'A')).toStream
    val adj = AdjacencyList(edges)
    val search = bfs(adj, 'A')
    search should be(Seq('A','B','C'))
  }

  test("`dfs` on missing vertex returns empty sequence") {
    val edges = Random.shuffle(List('A' --> 'B', 'A' --> 'C', 'B' --> 'D', 'C' --> 'D')).toStream
    val adj = AdjacencyList(edges)
    val search = dfs(adj, 'E')
    search should be(Seq.empty[Char])
  }

  test("`dfs` returns correct ordering for simple graph") {
    val edges = Random.shuffle(List('A' --> 'B', 'A' --> 'C', 'B' --> 'D', 'C' --> 'D')).toStream
    val adj = AdjacencyList(edges)
    val search = dfs(adj, 'A')
    val expected = Set(Seq('A', 'B', 'D', 'C'), Seq('A', 'C', 'D', 'B'))
    expected.contains(search) should be(true)
  }

  test("`bfs` returns correct ordering for simple graph") {
    val edges = Random.shuffle(List('A' --> 'B', 'A' --> 'C', 'B' --> 'D', 'C' --> 'D')).toStream
    val adj = AdjacencyList(edges)
    val search = bfs(adj, 'A')
    val expected = Set(Seq('A', 'C', 'B', 'D'), Seq('A', 'B', 'C', 'D'))
    expected.contains(search) should be(true)
  }


}
