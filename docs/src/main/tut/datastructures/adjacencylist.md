---
layout: docs 
title:  "AdjacencyList"
section: "datastructures"
source: "core/src/main/scala/tiki/AdjacencyList.scala"
scaladoc: "#tiki.AdjacencyList"
---
# AdjacencyList

`AdjacencyList` associates each vertex in the graph with the collection of its neighboring vertices.
Internally this is represented as a pair of maps (the graph and its reverse). As implied by
the method names, structures using this for undirected graphs should construct the `AdjacencyList` with the reverse
graph. _This may change in a later version_.

The functions provided are:

- `successors(v)` returns the child vertices of the vertex _v_.
- `predecessors(v)` returns the parent vertices of the vertex _v_.

Both functions will return an Option, None will be returned if _v_ does not exist in the graph.
 
## Usage

### Constructing an adjacency list

An adjacency should be constructed using the implicit object `adjacencyList` which
will take an `Iterable` of any edge type and return an `AdjacencyList`.
 
### successors
 
```tut
import tiki._
import tiki.Predef._
import tiki.implicits._

val xs = buildAdjacencyList(List(1 --> 2, 1 --> 3, 2 --> 3))
val ys = xs.successors(1)
```
 
### predecessors
  
```tut
import tiki._
import tiki.Predef._
import tiki.implicits._

val xs = buildAdjacencyList(List(1 --> 2, 1 --> 3, 2 --> 3))
val ys = xs.predecessors(3)
```
  
