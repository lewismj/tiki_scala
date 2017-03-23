---
layout: docs 
title:  "Graph"
section: "datastructures"
source: "core/src/main/scala/tiki/Graph.scala"
scaladoc: "#tiki.GraphRep"
---
# Graph

Representing a graph as a specific data structure, such as an adjacency list or rose-tree may 
not always be desirable.
It is useful to split the algorithms on the graph from the underlying
representation.

There are two core traits, that define the graph behaviours.

Algorithms such as `dfs` will require something that supports the `Directed` interface only.

Note:
- In the future, may add a `Weighted` trait to allow weighted graphs with labelled edges.


## Usage

In the following example, for a simple test, the `WeightedDigraph` was defined using
an adjacency list and an edge list (containing weights). 

```tut
import tiki.Predef._
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
  def contains(v: Char) = adjacencyList.contains(v)
  def vertices: Stream[Char] = adjacencyList.vertices
  def successors(v: Char) = adjacencyList.successors(v)
  def predecessors(v: Char) = adjacencyList.predecessors(v)

  def edges: Stream[WeightedEdge[Char]] = xs.toStream
}
    
```

### Directed

```scala
trait Directed[A] {
  def contains(v: A): Boolean
  def successors(v: A): Set[A]
  def predecessors(v: A): Set[A]
}
```
With:

- `contains(v)` should return true if the vertex belongs in the graph.
- `successors(v)` the vertices that have an incoming edge *from* _v_.
- `predecessora(v)` the vertices that have an outgoing edge *to* _v_.

### Graph

The core _graph_ trait simply defines a graph as either a stream of _vertices_ and _edges_.

```scala
trait Graph[A,B] {
  def vertices: Stream[A]
  def edges: Stream[B]
}
```
- `edges` a stream of edges.
- `vertices` a stream of vertices

### Digraph

A _digraph_ is a graph that supports the `Directed` interface.

```scala
trait Digraph[A,B] extends Graph[A,B] with Directed[A] {}
```

### WeightedDigraph

The weighted digraph interface can be defined as `Digraph` with weighted edges.


```scala
trait WeightedDigraph[A] extends Digraph[A,WeightedEdge[A]] {}
```