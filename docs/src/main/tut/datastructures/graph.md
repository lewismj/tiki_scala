---
layout: docs 
title:  "Graph"
section: "datastructures"
source: "core/src/main/scala/tiki/Graph.scala"
scaladoc: "#tiki.GraphRep"
---
# Graph

Representing a graph as an adjacency list or rose-tree may not always be desirable.
Also, it is sometimes useful to split the algorithms on the graph from the underlying
representation. e.g. _Traversal_.

There are two core traits, `Directed` which is an interface that some structures such
as `AdjacencyList` may support. 

Algorithms such as `dfs` will require something that supports the `Directed` interface only.

## Directed Interface

```scala
trait Directed[A] {
  def vertices: Set[A]
  def contains(v: A): Boolean
  def successors(v: A): Set[A]
  def predecessors(v: A): Set[A]
}
```
With:

- `contains(v)` should return true if the vertex belongs in the graph.
- `successors(v)` the vertices that have an incoming edge *from* _v_.
- `predecessora(v)` the vertices that have an outgoing edge *to* _v_.

## Weighted

The weighted interface specifies that there must be a _weight_ between two vertices.

```scala
trait Weighted[A] {
  def weight(v: A, b: A): Double
}
```


## Graph Interface

The core _graph_ trait simply defines a graph as either a stream of _vertices_ and _edges_.

```scala
trait Graph[A,B] {
  def vertices: Stream[A]
  def edges: Stream[B]
}
```
- `edges` a stream of edges.
- `vertices` a stream of vertices

## Weighted Digraph

The weighted digraph interface can be defined as the trait:

```scala
trait WeightedDigraph[A] extends Graph[A,WEdge[A]] with Weighted[A] with Directed[A] 
```