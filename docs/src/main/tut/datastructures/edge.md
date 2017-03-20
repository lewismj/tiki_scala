---
layout: docs 
title:  "Edge"
section: "datastructures"
source: "core/src/main/scala/tiki/Edge.scala"
scaladoc: "#tiki.Edge"
---
# Edges

There are currently two `Edge` types, `Edge` and `LEdge`. An `Edge` represents a
directed edge between two vertices. A labelled edge `LEdge` represents an edge
with a label.

Undirected edges? _Unless explicitly stated_ most algorithms would assume an undirected
 edge be represented by two directed edges. 
 
Why? Many practical use cases tend to be concerned with directed graphs, and usually directed
acyclic graphs (DAG) that are connected, i.e. _Trees_. 

_Often vertex type of an Edge may be a proxy for some underlying vertex type.
Where an instance of the underlying type may be costly to hold within a graph._
 
 
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