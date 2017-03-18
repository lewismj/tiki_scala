---
layout: docs 
title:  "Edge"
section: "datastructures"
source: "core/src/main/scala/tiki/Edge.scala"
scaladoc: "#tiki.Edge"
---
# Edges

`Edge` is a case class that represents an edge between two vertices. The edges in
a graph tend to be either all directed or undirected. However, almost all practical
use cases tend to be concerned with directed graphs, and usually directed
acyclic graphs (DAG) that are connected, i.e. _Trees_. Most data structures assume
the edge is directed 'from' a vertex 'to' another vertex, _unless explicitly stated_.


_Often vertex type of an Edge may be a proxy for some underlying vertex type.
Where an instance of the underlying type may be costly to hold within a graph._
 
Each edge type must support a simple map function.

- `map` apply the function that takes an edge and returns another edge.

 
 In addition, the labelled edge`LEdge` supports a map function over the label.
 
 - `lmap` apply the function `f` on the label and return a new labelled edge.
 

## Usage

Two simple edge case classes are provided. _Edge_ and _LEdge_, representing an 
edge or labelled edge.

### Edge

A trait defines the behaviour that edges must exhibit, at a minimum an edge should link two vertices.
Each edge class must provde a simple `map` function 

```scala
trait EdgeLike[A] {
  def from: A
  def to: A
  def map[B](f: EdgeLike[A] => EdgeLike[B]): EdgeLike[B]
}
```

Defined as follows:
```scala
case class Edge[A](from: A, to: A) extends EdgeLike[A] {
  def map[B](f: EdgeLike[A] => EdgeLike[B]): EdgeLike[B] = f(this)
}
```

```tut
import tiki._
import tiki.Predef._

val edge = Edge[Int](1,2)
```

### LEdge

The _labelled_ edge case class.

```scala
case class LEdge[A,B](edge: Edge[A], label: B)  extends EdgeLike[A] {
   def from : A = edge.from
   def to: A = edge.to
   def map[C](f: EdgeLike[A] => EdgeLike[C]): EdgeLike[C] = LEdge(f(edge),label)
   def lmap[C](f: B => C) : LEdge[A,C] = LEdge(edge,f(label))
}
```

The label can be any type, below a _weighted_ edge is represented as 
an edge labelled with a `Double`.

```tut
import tiki._
import tiki.Predef._

val edge = Edge[Int](1,2)
val weightedEdge = LEdge(edge,2.23)
```