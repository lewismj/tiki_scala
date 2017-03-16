---
layout: docs 
title:  "Edge"
source: "core/src/main/scala/Edge.scala"
---
# Edges

`Edge` is a case class that represents an edge between two vertices. The edges in
a graph tend to be either all directed or undirected. So the edge case class itself does 
not distinguish (i.e. directed or undirected _edges_ is usually within the context of 
 a particular graph).
 

_Often vertex type may be a proxy for some underlying vertex type.
Where instance of the type may be costly to hold within a graph._
 

 
 The labelled edge`LEdge` supports a map function:
 
 - `map` apply the function `f` on the label and return a new labelled edge.
 

## Usage

Two simple edge case classes are provided. _Edge_ and _LEdge_, representing an 
edge or labelled edge.

### Edge

A trait defines the behaviour that edges must exhibit, at a minimum an edge should link two vertices.


```scala
trait EdgeLike[A] {
  def from: A
  def to: A
}
```

Defined as follows:
```scala
case class Edge[A](from: A, to: A) extends EdgeLike[A]
```

```tut
import tiki._
import tiki.Predef._

// An edge between two vertices, 1 and 2.
val edge = Edge[Int](1,2)
```

#### LEdge

The _labelled_ edge case class, is defined as follows:

```scala
case class LEdge[A,B](edge: Edge[A], label: B)  extends EdgeLike[A] {
  def map[C](f: B => C) : LEdge[A,C] = LEdge(edge,f(label))
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
