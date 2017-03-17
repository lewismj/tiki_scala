---
layout: docs 
title:  "Edge"
section: "datastructures"
source: "core/src/main/scala/tiki/Edge.scala"
scaladoc: "#tiki.Edge"
---
# Edges

`Edge` is a case class that represents an edge between two vertices. The edges in
a graph tend to be either all directed or undirected. Most data structures assume
the edge is directed 'from' a vertex 'to' another vertex, _unless explicitly stated_.
 

_Often vertex type may be a proxy for some underlying vertex type.
Where instance of the type may be costly to hold within a graph._
 
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

// An edge between two vertices, 1 and 2.
val edge = Edge[Int](1,2)
```

#### LEdge

The _labelled_ edge case class, is defined as follows:

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

The package object defines two utility functions in cases where `Edge` represents a directed edge.

```scala
def reverse[A](e: EdgeLike[A]): Edge[A] = Edge(e.to,e.from)
def reverseAll[A](edges: Iterable[EdgeLike[A]]) :Iterable[EdgeLike[A]]= edges.map(_.map(reverse))
  ```
  
