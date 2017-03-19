---
layout: docs 
title:  "Edge"
section: "datastructures"
source: "core/src/main/scala/tiki/Edge.scala"
scaladoc: "#tiki.Edge"
---
# Edges

To represent edges we have a base trait `EdgeLike`, together with case classes `Edge`
and `LEdge`. `EdgeLike` is a trait that defines two methods `to` and `from`. 
This can be used to represent a directed edge. If dealing with undirected graph, we
can either explicitly add a reversed edge.
 
Many practical use cases tend to be concerned with directed graphs, and usually directed
acyclic graphs (DAG) that are connected, i.e. _Trees_. Most data structures assume
the edge is directed 'from' a vertex 'to' another vertex, _unless explicitly stated_.

`Edge` is a case class that represents an edge between two vertices. 

_Often vertex type of an Edge may be a proxy for some underlying vertex type.
Where an instance of the underlying type may be costly to hold within a graph._
 
`LEdge` is a case class that represents an edge with a label attached.
 
 The labelled edge`LEdge` supports a map function over the label.
 
- `lmap` apply the function `f` on the label and return a new labelled edge.
 

## Usage

### Constructing Edges

Edges can be constructed directly. However, importing `implicits` 
will allow you to use the `-->` and `--> :+` operators.


#### Edge

```tut
import tiki._
import tiki.Predef._
import tiki.implicits._

val e0 = Edge[Int](1,2)

// Use the edge --> operator
val e1 = 1 --> 2
```

#### LEdge

The label can be any type, below a _weighted_ edge is represented as an edge labelled with a `Double`.

```tut
import tiki._
import tiki.Predef._

val e0 = Edge[Int](1,2)
val we0 = LEdge(e0,2.23)

// User the :+ to apply a label to an edge.
val we1 = (1 --> 2 :+ 2.23)
```