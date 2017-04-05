---
layout: docs 
title:  "Weighted Graph"
section: "typeclasses"
source: "core/src/main/scala/tiki/Graph.scala"
scaladoc: "#tiki.Graph"
---
## Weighted graphs

There are two types of weighted graphs, one based on `Digraph` the other
on `Graph` (i.e. non directed graph).


#### WeightedDigraph

The weighted digraph interface can be defined as a`Digraph` that returns
 weighted`edges`. 
 
  
```scala
trait WeightedDigraph[A] extends Digraph[A]  {
  override def edges: Stream[WeightedEdge[A]]
}
```
 
#### WeightedUndirectedGraph

The weighted undirected graph is defined as a graph that returns weighted edges.

```scala
trait WeightedUndirectedGraph[A] extends Graph[A] {
  def edges: Stream[WeightedEdge[A]]
}
```

Rather than force a common point in a type hierarchy, a `WeightedGraph` type-class is
defined:

```scala
trait WeightedGraph[A] extends Graph[A] {
  def edges: Stream[WeightedEdge[A]]
}


object WeightedGraph {
  def apply[A: WeightedGraph]: WeightedGraph[A] = implicitly
}
```

Both types of weighted graph implicitly support this interface (_tiki.implicits_).

```scala
  implicit def undirected[A](g: WeightedUndirectedGraph[A]): WeightedGraph[A] = new WeightedGraph[A] {
    def vertices: Stream[A] = g.vertices
    def edges: Stream[WeightedEdge[A]] = g.edges
  }
```

```scala
  implicit def directed[A](g: WeightedDigraph[A]): WeightedGraph[A] = new WeightedGraph[A] {
    def edges: Stream[WeightedEdge[A]] = g.edges
    def vertices: Stream[A] = g.vertices
  }
```