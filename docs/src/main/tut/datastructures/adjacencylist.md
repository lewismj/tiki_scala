---
layout: docs 
title:  "AdjacencyList"
section: "datastructures"
source: "core/src/main/scala/tiki/AdjacencyList.scala"
scaladoc: "#tiki.AdjacencyList"
---
# AdjacencyList

`AdjacencyList` associates each vertex in the graph with the collection of its neighboring vertices.
Internally this is represented as a map with key being the vertex and values being its neighbours.
The reverse graph is also stored as map.

The functions provided are:

- 'children(_v_)' returns the child vertices of the vertex _v_.
- 'parents(_v_)' returns the parent vertices of the vertex _v_.
- both functions will return an Option, None will be returned if _v_ does not exist in the graph.
 
 ## Usage
 
 ### Constructing an adjacency list
 
 An adjacency list can be constructed as follows:
 
 ```tut
 import tiki._
 import tiki.Predef._
 
 val adjacencyList = AdjacencyList(List(Edge(1,2),Edge(1,3),Edge(2,3)))
 ```
 
 ### Children
 
Return the child vertices:

 ```tut
 import tiki._
 import tiki.Predef._
 
 val adjacencyList = AdjacencyList(List(Edge(1,2),Edge(1,3),Edge(2,3)))
 val xs = adjacencyList.children(1)
 ```
 
  ### Parents
  
  Return the parent vertices:
  
```tut
import tiki._
import tiki.Predef._

val adjacencyList = AdjacencyList(List(Edge(1,2),Edge(1,3),Edge(2,3)))
val xs = adjacencyList.parents(3)
```
  
