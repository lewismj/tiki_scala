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
 - `kruskal(g)` finds the minimum spanning tree of a weighted graph, using Kruskal's algorithm.

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

The graph:

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/negativeCyle.png)

Will have a negative cycle (**reachable from _a_**) between _b_, _e_ and _d_.

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

val cycle = negativeCycle(digraph,'a')
cycle.mkString
```

### Kruskal's Algorithm

From [wikipedia](https://en.wikipedia.org/wiki/Kruskal%27s_algorithm)

"_Kruskal's algorithm is a minimum-spanning-tree algorithm which finds an edge 
of the least possible weight that connects any two trees in the forest.
 It is a greedy algorithm in graph theory as it finds a minimum spanning tree
 for a connected weighted graph adding increasing cost arcs at each step.
 This means it finds a subset of the edges that forms a tree that includes every vertex,
  where the total weight of all the edges in the tree is minimized. 
  If the graph is not connected, then it finds a minimum spanning forest 
  (a minimum spanning tree for each connected component)_."

This is implemented as a fold over the edges of a weighted graph.

```scala
case class SpanState[A](ds: DisjointSet[A], mst: List[WeightedEdge[A]])

object SpanState {
def empty[A](g: WeightedGraph[A]): SpanState[A]
  = new SpanState[A](DisjointSet(g.vertices.toSet),List.empty[WeightedEdge[A]])
}
  
def kruskal[A](g: WeightedGraph[A]): List[WeightedEdge[A]] =
  g.edges.sortBy(_.weight).foldLeft(SpanState.empty(g))((state,y) => y.edge match {
    case Edge(u,v) if state.ds.find(u) != state.ds.find(v) =>
      new SpanState(state.ds.union(u,v).getOrElse(state.ds),y :: state.mst)
    case _ => state
  }).mst  
```

An example minimum spanning tree is given below:

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/minimumSpanningTree.png)

```tut
import tiki.Predef._
import tiki.implicits._
import tiki.Path._

val xs = Stream(
  'a' --> 'b' :# 7.0,
  'a' --> 'd' :# 5.0,
  'b' --> 'c' :# 8.0,
  'b' --> 'e' :# 7.0,
  'c' --> 'e' :# 5.0,
  'd' --> 'b' :# 9.0,
  'd' --> 'e' :# 15.0,
  'd' --> 'f' :# 6.0,
  'e' --> 'f' :# 8.0,
  'e' --> 'g' :# 9.0,
  'f' --> 'g' :# 11.0
)


val graph = new WeightedUndirectedGraph[Char] {
  override def edges: Stream[WeightedEdge[Char]] = xs
  override def vertices: Stream[Char] = Stream('a', 'b', 'c', 'd', 'e', 'f', 'g')
}

val expected = Set(
  'a' --> 'd' :# 5.0,
  'c' --> 'e' :# 5.0,
  'd' --> 'f' :# 6.0,
  'a' --> 'b' :# 7.0,
  'b' --> 'e' :# 7.0,
  'e' --> 'g' :# 9.0
)

val k = kruskal(graph)
k.mkString("\n")
```