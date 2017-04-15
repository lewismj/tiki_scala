---
layout: docs 
title:  "Strongly Connected Components"
section: "algorithms"
source: "core/src/main/scala/tiki/Components.scala"
scaladoc: "#tiki.Components"
---
# Strongly Connected Components

From [wikipedia](https://en.wikipedia.org/wiki/Strongly_connected_component)

"_In the mathematical theory of directed graphs, a graph is said to be strongly connected or diconnected if
 every vertex is reachable from every other vertex. The strongly connected components or diconnected
  components of an arbitrary directed graph form a partition into subgraphs that are 
  themselves strongly connected. It is possible to test the strong connectivity of a graph, 
  or to find its strongly connected components, in linear time._"

## Kosaraju's Algorithm 


1. Given a digraph _g_ and empty stack _s_.  While _s_ does not contain all vertices:

    - Pick a vertex _v_ of _g_ not in _s_, perform a depth first search starting at _v_,
    each time that the search finishes expanding a vertex _u_ push _u_ onto _s_.
    
2. Transpose the graph.

3. While _s_ is non-empty:

    - Take the vertex _v_ from the head of _s_. Perform a depth first search start at _v_.
    The set of visited vertices will be the strongly connected component containing _v_.
    Remove these vertices from the transpose and _s_.
  
## Example

The following graph shows the three strongly connected components:

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/scc.png)

```tut
import tiki._
import tiki.Predef._
import tiki.Components._
import tiki.implicits._

val xs = Stream(
  1 --> 2,
  2 --> 3,
  2 --> 4,
  2 --> 5,
  3 --> 6,
  4 --> 5,
  4 --> 7,
  5 --> 2,
  5 --> 6,
  5 --> 7,
  6 --> 3,
  6 --> 8,
  7 --> 8,
  7 --> 10,
  8 --> 7,
  9 --> 7,
  10 --> 9,
  10 --> 11,
  11 --> 12,
  12 --> 10
)

val adj = AdjacencyList(xs)
val g = new Digraph[Int] {
  override def contains(v: Int): Boolean = adj.contains(v)
  override def successors(v: Int): Set[Int] = adj.successors(v)
  override def predecessors(v: Int): Set[Int] = adj.predecessors(v)
  override def vertices: Stream[Int] = adj.vertices
  override def edges: Stream[EdgeLike[Int]] = xs
}

val scc = kosaraju(g).toSet
scc.mkString(",")
```

## Implementation  

```scala
  def kosaraju[A](g: Digraph[A]): List[List[A]] = {

    @tailrec
    def loop(gr: Digraph[A], s: List[A], scc: List[List[A]]): List[List[A]] = s match {
      case Nil => scc
      case head :: tail =>
        val component = dfs(gr,head).toList
        loop(remove(gr,component), s.diff(component), component :: scc)
    }

    val stack = g.vertices.foldLeft(List.empty[A])((a,v) => dfs(remove(g,a),v).toList ::: a)

    loop(g.transpose,stack,List.empty[List[A]])
  }
```