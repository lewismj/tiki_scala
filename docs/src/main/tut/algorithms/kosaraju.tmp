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
import tiki.implicits._
import tiki.Components._

val xs =  Stream(
  1 --> 0,
  0 --> 2,
  2 --> 1,
  0 --> 3,
  3 --> 4
)

val adj = AdjacencyList(xs)

val g = new Digraph[Int] {
  override def contains(v: Int): Boolean = adj.contains(v)
  override def successors(v: Int): Set[Int] = adj.successors(v)
  override def predecessors(v: Int): Set[Int] = adj.predecessors(v)
  override def vertices: Stream[Int] = adj.vertices
  override def edges: Stream[EdgeLike[Int]] = xs
}

val expected = Set(List(4), List(3), List(0, 1, 2))
kosaraju(g).mkString(",")
```

## Implementation  (Work in Progress, Experimental)

For step 2, we can reuse the `traverse` function used by the depth first search:

```scala
def traverse[A](g: Directed[A], l: List[A], dfs: Boolean): Stream[A]
  = unfold( (l,Set.empty[A]) ) { ... }
```

When a depth first search is performed from a single vertex _start_ we call `traverse`, passing
in a single element list `List(start)` for _l_. 

In order to satisfy step 2, we can `unfold` using the complete list of vertices in the graph
as our initial starting point. Each call to unfold will prepend the depth first search of a vertex,
until all vertices have been searched. The call to `distinct` will preserve the ordering.

i.e.
```scala
  traverse(g, g.vertices.toList, dfs=true).distinct.toList
 ```
