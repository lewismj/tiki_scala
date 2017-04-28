---
layout: docs 
title:  "Minimum Spanning Tree"
section: "algorithms"
source: "core/src/main/scala/tiki/Path.scala"
scaladoc: "#tiki.Path"
---
# Minimum Spanning Tree

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

Optimisations can be made if the points on the graph represent Euclidean distances (_further work_).

## Example

The edges of the minimum spanning tree are highlighted below:

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/minimumSpanningTree.png)

## Functions

 - `kruskal(g)` finds the minimum spanning tree of a weighted graph, using Kruskal's algorithm.

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


```tut
import tiki._
import tiki.Path._
import tiki.implicits._


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