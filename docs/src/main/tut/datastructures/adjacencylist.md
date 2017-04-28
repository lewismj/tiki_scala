---
layout: docs 
title:  "Adjacency List"
section: "datastructures"
source: "core/src/main/scala/tiki/AdjacencyList.scala"
scaladoc: "#tiki.AdjacencyList"
---
# Adjacency List

`AdjacencyList` associates each vertex in the graph with the collection of its neighboring vertices.
Internally this is represented as a pair of maps (the graph and its reverse). 

## Example

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/adjacencyList.png)


The functions provided are:

- `successors(v)` returns the child vertices of the vertex _v_.
- `predecessors(v)` returns the parent vertices of the vertex _v_.

Both functions will return an Option, None will be returned if _v_ does not exist in the graph.
 
## Usage

The adjacency list is usually used to help implement one of the `Graph` interfaces.
 
### successors
 
```tut
import tiki._
import tiki.implicits._

val xs = AdjacencyList(Stream(1 --> 2, 1 --> 3, 2 --> 3))
val ys = xs.successors(1)
```
 
### predecessors
  
```tut
import tiki._
import tiki.implicits._

val xs = AdjacencyList(Stream(1 --> 2, 1 --> 3, 2 --> 3))
val ys = xs.predecessors(3)
```
  
