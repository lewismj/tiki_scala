---
layout: docs 
title:  "Graph"
section: "datastructures"
source: "core/src/main/scala/tiki/Graph.scala"
scaladoc: "#tiki.Graph"
---
# Graph

Representing a graph as a specific data structure, such as an adjacency list or rose-tree may 
not always be desirable.

It is useful to split the algorithms on the graph from the underlying representation.

A number of core traits define the graph behaviours.

Algorithms (generally) should require the _minimal_ interface as function arguments.


## Implementation

The core _graph_ trait simply defines a graph as streams of _vertices_ and _edges_.

```scala
trait Graph[A] {
  def vertices: Stream[A]
  def edges: Stream[EdgeLike[A]]
}
```
- `edges` a stream of edges.
- `vertices` a stream of vertices

#### Directed

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

#### Digraph

A _digraph_ is a graph that supports the `Directed` interface.

```scala
trait Digraph[A] extends Graph[A] with Directed[A] {}
```

## Weighted 

For convenience (many algorithms just require the set of weighted edges, rather than
say a function _weight(u,v)_), the weighted interface is defined as follows:

```scala
trait Weighted[A] {
  def edges: Stream[WeightedEdge[A]]
}
```

The definition for weighted graphs (n.b. they don't form a sub-hierarchy) is as follows:

```scala
trait WeightedUndirectedGraph[A] extends Graph[A] with Weighted[A]
trait WeightedDigraph[A] extends Digraph[A] with Weighted[A]
trait WeightedGraph[A] extends Graph[A] with Weighted[A]
```


## Usage

In the following example, the `WeightedDigraph` interface is implemented using the `AdjacencyList`.
Sometimes this may be the correct choice, though not always.

```tut
import tiki._
import tiki.implicits._


val xs = Stream(
  'A' --> 'B' :# -1.0,
  'A' --> 'C' :# 4.0,
  'B' --> 'C' :# 3.0,
  'B' --> 'D' :# 2.0,
  'D' --> 'B' :# 1.0,
  'B' --> 'E' :# 2.0,
  'E' --> 'D' :# -3.0
)
val adjacencyList = AdjacencyList(xs)

val digraph = new WeightedDigraph[Char] {
  def contains(v: Char) = adjacencyList.contains(v)
  def vertices: Stream[Char] = adjacencyList.vertices
  def successors(v: Char) = adjacencyList.successors(v)
  def predecessors(v: Char) = adjacencyList.predecessors(v)

  def edges: Stream[WeightedEdge[Char]] = xs
}
    
```

