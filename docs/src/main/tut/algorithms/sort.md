---
layout: docs 
title:  "Sort"
section: "algorithms"
source: "core/src/main/scala/tiki/Sort.scala"
scaladoc: "#tiki.Sort"
---
# Topological Sorting

From [wikipedia](https://en.wikipedia.org/wiki/Topological_sorting)

"_A topological sort of a directed graph is a linear
ordering of its vertices such that for every directed edge uv 
from vertex u to vertex v, u comes before v in the ordering_."

A topological sort (or ordering) is only possible if the digraph contains
no cycles. That is, we have a directed acyclic graph.

The current function will return _None_ if a cycle is found.

## Kahn's Algorithm

See the wikipedia link for references. Implementation follows Kahn's algorithm.

```scala
  def tsort[A](g: Digraph[A]): Option[Stream[A]] = {
    @tailrec
    def kahn(s0: Stream[A], l: Stream[A], ys: Stream[EdgeLike[A]]): Option[Stream[A]] = s0 match {
      case _ if (s0 isEmpty) && (ys isEmpty) => Some(l)
      case _ if s0 isEmpty => None
      case n #:: tail =>
        val (edgesFrom, remainder) = ys.partition(_.from == n)
        val insert = edgesFrom.map(_.to).filterNot(remainder.map(_.to).contains(_))
        kahn(tail #::: insert, l :+ n, remainder)
    }
    kahn(g.vertices.filterNot(g.edges.map(_.to).contains(_)), Stream.empty, g.edges)
  }
```

### Example

A topological sort of the graph ...

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/topologicalSort.png)

... should yield the set  _{a,b,c,d,e,f}_

```tut
import tiki._
import tiki.Predef._
import tiki.implicits._
import tiki.Sort._


val xs = Stream (
    'a' --> 'b',
    'a' --> 'c',
    'b' --> 'c',
    'b' --> 'd',
    'c' --> 'd',
    'd' --> 'e',
    'd' --> 'f',
    'e' --> 'f'
)

val adjacencyList = AdjacencyList(xs)
val digraph = new Digraph[Char] {
  def contains(v: Char) = adjacencyList.contains(v)
  def vertices: Stream[Char] = adjacencyList.vertices
  def successors(v: Char) = adjacencyList.successors(v)
  def predecessors(v: Char) = adjacencyList.predecessors(v)
  def edges: Stream[Edge[Char]] = xs
}

val sorted = tsort(digraph).getOrElse(Stream.empty)
sorted.mkString
```