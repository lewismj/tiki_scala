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
- `negativeCycle(g,source)` discovers a negative cycle (or returns an empty list if none exist) in the graph
 _g_ starting from vertex _source_.

### Bellman-Ford

The path state is represented as a case class, holding _distances_ and _predecessors_.
```scala
  case class PathState[A](distances: Map[A,Double], predecessors: Map[A,A])

  object PathState {
    def apply[A](source: A): PathState[A] =
      PathState(Map.empty[A,Double].updated(source,0.0),Map.empty[A,A])
  }
```
The algorithm is implemented (with _N_ iterations). _note_ I think almost all 
use cases would allow us to stop once
```scala
  def bellmanFord[A](g: WeightedDigraph[A], source: A): PathState[A] = {
    
    def relaxEdge(state: PathState[A], e: WeightedEdge[A]): PathState[A] =
      state.distances.getOrElse(e.from, ∞) match {
        case du if du < ∞ =>
          val dv = state.distances.getOrElse(e.to, ∞)
          if (du + e.weight < dv) {
            val w0 = max(⧞,du + e.weight)
            val d = state.distances.updated(e.to,w0)
            val p = state.predecessors.updated(e.to, e.from)
            PathState(d, p)
          } else state
        case _ => state
      }

    g.vertices.indices.foldLeft(PathState(source))(
      (xs, _) => g.edges.foldLeft(xs)(relaxEdge))
  }
```


### Negative cycles
A utility method to search for negative cycles is provided:
```scala
  def negativeCycle[A](g: WeightedDigraph[A], source: A): List[A] = {
    val s = bellmanFord(g, source)
    g.edges.flatMap {
      case e if s.distances(e.from) + e.weight < s.distances(e.to) => Some(e.to)
      case _ => None
    } match {
      case head #:: tail if tail contains source => predecessorList(s, source)
      case head #:: _ => predecessorList(s, head)
      case _ => List.empty[A]
    }
  }
```

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/cycle.png)


```tut
import tiki._
import tiki.Predef._
import tiki.Path._
import tiki.implicits._

val xs = Stream(
  'A' --> 'B' :# -1.0,
  'A' --> 'C' :# 4.0,
  'B' --> 'C' :# 3.0,
  'B' --> 'D' :# 2.0,
  'D' --> 'B' :# 1.0,
  'B' --> 'E' :# 2.0,
  'E' --> 'D' :# -6.0
)
val adjacencyList = AdjacencyList(xs)

val digraph = new WeightedDigraph[Char] {
  /* Use adjacency list for basic digraph implementation. */
  def contains(v: Char) = adjacencyList.contains(v)
  def vertices: Stream[Char] = adjacencyList.vertices
  def successors(v: Char) = adjacencyList.successors(v)
  def predecessors(v: Char) = adjacencyList.predecessors(v)
  /* adjacency doesn't store edges. */
  def edges = xs
}

val state = bellmanFord(digraph,'A')
state.distances
```
