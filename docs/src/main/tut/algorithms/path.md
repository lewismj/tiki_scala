---
layout: docs 
title:  "Negative Cycle Detection"
section: "algorithms"
source: "core/src/main/scala/tiki/Path.scala"
scaladoc: "#tiki.Path"
---
# Negative Cycle Detection

A negative cycle in a graph is a cycle whose edges sum to a negative value.

## Example
The graph:

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/negativeCycle.png)

Will have a negative cycle (**reachable from _A_**) between _B_, _E_ and _D_.

## Functions

-  `bellmanFord(g,start)` performs Bellman-Ford algorithm to find shortest distances from source,
given a weighted directed graph and source vertex.
- `negativeCycle(g,source)` discovers a negative cycle (or returns an empty list if none exist) in the graph
 _g_ starting from vertex _source_.


The path state is a case class of the _distances_ and _predecessors_.

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

    Range(1,g.vertices.size).foldLeft(PathState(source))((xs, _)
     => g.edges.foldLeft(xs)(relaxEdge))
  }
```


### Negative cycles

A utility method to search for negative cycles is provided.

```scala
  def negativeCycle[A](g: WeightedDigraph[A], source: A): Option[List[A]] = {
    val s = bellmanFord(g, source)
    g.edges.flatMap {
      case e if s.distances.getOrElse(e.from,∞) + e.weight <
                s.distances.getOrElse(e.to,∞) => Some(e.from)
      case _ => None
    } match {
      case head #:: _ => Some(predecessorList(s,head))
      case _ => None
    }
```

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

val digraph = new WeightedDigraph[Char] {
  lazy val ys = AdjacencyList(xs)
  def contains(v: Char) = ys.contains(v)
  def vertices: Stream[Char] = ys.vertices
  def successors(v: Char) = ys.successors(v)
  def predecessors(v: Char) = ys.predecessors(v)
  def edges = xs
}

val cycle = negativeCycle(digraph,'a')
cycle.mkString
```