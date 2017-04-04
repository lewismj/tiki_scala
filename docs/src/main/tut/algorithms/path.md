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
A utility method to search for negative cycles is provided.

**note** At present it just returns the details of the `predecessor` 
list from the Bellman-Ford output. This will contain the negative 
cycle, but may not be limited to just the cycle.

```scala
  def negativeCycle[A](g: WeightedDigraph[A], source: A): Option[List[A]] = {
    val s = bellmanFord(g, source)
    g.edges.flatMap {
      case e if s.distances.getOrElse(e.from,∞) + e.weight <
                s.distances.getOrElse(e.to,∞) => Some(e.to)
      case _ => None
    } match {
      case edges if edges contains source => Some(predecessorList(s,source))
      case edges if edges.nonEmpty => Some(predecessorList(s,edges.last))
      case _ => None
    }
  }
```

The graph ...

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/cycle.png)

Will have a negative cycle, reachable from 'a', the predecessor list returned will contain
{e,d,b}. n.b. If the order of the edges is changed, 'c' has a predecessor of 'b'. 
The method can be improved to return _just_ the cycle, when this happens.

```tut
import tiki._
import tiki.Predef._
import tiki.Path._
import tiki.implicits._

val xs = Stream(
        'a' --> 'b' :# -1.0,
        'a' --> 'c' :# 4.0,
        'b' --> 'c' :# 3.0,
        'b' --> 'd' :# 2.0,
        'd' --> 'b' :# 1.0,
        'b' --> 'e' :# 2.0,
        'e' --> 'd' :# -6.0
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

val cycle = negativeCycle(digraph,'A')
cycle.mkString
```
