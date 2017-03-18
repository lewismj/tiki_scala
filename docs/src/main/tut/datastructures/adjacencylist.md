---
layout: docs 
title:  "AdjacencyList"
section: "datastructures"
source: "core/src/main/scala/tiki/AdjacencyList.scala"
scaladoc: "#tiki.AdjacencyList"
---
# AdjacencyList

`AdjacencyList` associates each vertex in the graph with the collection of its neighboring vertices.
Internally this is represented as a pair of maps (the graph and its reverse).

The functions provided are:

- `successors(v)` returns the child vertices of the vertex _v_.
- `predecessors(v)` returns the parent vertices of the vertex _v_.

Both functions will return an Option, None will be returned if _v_ does not exist in the graph.
 
## Usage

### Constructing an adjacency list

An adjacency list can be constructed as follows:

```tut
import tiki._
import tiki.Predef._

val adjacencyList = AdjacencyList(List(Edge(1,2),Edge(1,3),Edge(2,3)))
```
 
### successors
 
```tut
import tiki._
import tiki.Predef._

val adjacencyList = AdjacencyList(List(Edge(1,2),Edge(1,3),Edge(2,3)))
val xs = adjacencyList.successors(1)
```
 
### predecessors
  
```tut
import tiki._
import tiki.Predef._

val adjacencyList = AdjacencyList(List(Edge(1,2),Edge(1,3),Edge(2,3)))
val xs = adjacencyList.predecessors(3)
```
  
