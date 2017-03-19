---
layout: docs 
title:  "Traversal"
section: "algorithms"
source: "core/src/main/scala/tiki/Traversal.scala"
scaladoc: "#tiki.Traversal"
---
# Traversal functions

Two primary functions are available:

- `dfs(g,start,stop)` will perform a depth-first traversal of the graph _g_, starting at _start_ vertex.
Optionally a _stop_ parameter can be specified. The traversal will stop when the _stop_ vertex is visited.
The sequence of traversal is returned.

- `bfs(g,start,stop)` as above, except a breadth first search will be done.

The implementation is quite small (See ScalaDoc for comments):

If _start_ is not contained in the graph, then the traversals will return an empty sequence.
If _stop_ is not contained in the graph, a full traversal from _start_ will be returned.

## Usage

### dfs
```tut
import tiki._
import tiki.Predef._
import tiki.Traversal._
import tiki.implicits._
import scala.util.Random

val edges = Random.shuffle(List('A' --> 'B', 'A' --> 'C', 'B' --> 'D', 'C' --> 'D'))
val search = dfs(AdjacencyList(edges),'A')
```

### bfs
```tut
import tiki._
import tiki.Predef._
import tiki.Traversal._
import tiki.implicits._
import scala.util.Random

val edges = Random.shuffle(List('A' --> 'B', 'A' --> 'C', 'B' --> 'D', 'C' --> 'D'))
val search = bfs(AdjacencyList(edges),'A')
```
