---
layout: docs 
title:  "Path"
section: "algorithms"
source: "core/src/main/scala/tiki/Path.scala"
scaladoc: "#tiki.Path"
---
# Path Functions

The following functions are currently implemented

-  `bellmanFord(g,start)` performs Bellman-Ford algorithm to find shortest distances from source,
given a weighted directed graph and source vertex.

### Bellman-Ford

The path state is represented as a case class.
```scala
case class PathState[A](distances: Map[A,Double], predecessors: Map[A,A])
```

The algorithm is simple to implement, as follows:
```scala
def bellmanFord[A](g: WeightedDigraph[A], source: A): PathState[A]
= g.vertices.foldLeft(PathState.empty[A].update(source,0))((xs, x) => {
    g.edges.foldLeft(xs)((ys, y) => {
      val (u,v,w) = (y.from,y.to,y.weight)
      val du = ys.distances.getOrElse(u,Double.PositiveInfinity)
      val dv = ys.distances.getOrElse(v,Double.PositiveInfinity)
      (du,dv,w) match {
        case _ if du + w < dv => ys.update(v,du+w,u)
        case _ => ys
      }
    })
  })
```

```tut
import tiki._
import tiki.Predef._
import tiki.Path._
import tiki.implicits._

val xs = List(
  'A' --> 'B' :# -1.0,
  'A' --> 'C' :# 4.0,
  'B' --> 'C' :# 3.0,
  'B' --> 'D' :# 2.0,
  'D' --> 'B' :# 1.0,
  'B' --> 'E' :# 2.0,
  'E' --> 'D' :# -3.0
)
val adjacencyList = buildAdjacencyList(xs)


val digraph = new WeightedDigraph[Char] {
  /* Use adjacency list for basic digraph implementation. */
  def contains(v: Char) = adjacencyList.contains(v)
  def vertices: Stream[Char] = adjacencyList.vertices
  def successors(v: Char) = adjacencyList.successors(v)
  def predecessors(v: Char) = adjacencyList.predecessors(v)
  /* adjacency doesn't store edges. */
  def edges: Stream[WeightedEdge[Char]] = xs.toStream
}

val state = bellmanFord(digraph,'A')
state.distances
```