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
Optionally a _stop_ parameter can be specified. The traversal will stop the _stop_ vertex is visited.
The sequence of traversal is returned.

- `bfs(g,start,stop)` as above, except a breadth first search will be done.

The implementation is quite small (See ScalaDoc for comments):

If _start_ is not contained in the graph, then the traversals will return an empty sequence.
If _stop_ is not contained in the graph, a full traversal from _start_ will be returned.
```scala
private def traverse[A](g: GraphRep[A], start: A, stop: Option[A])(f: S[A]): Seq[A] = {
@tailrec
def traverse0(remaining: Seq[A], visited: Set[A], acc: Seq[A]): Seq[A] = remaining match {
  case xs if xs.isEmpty => acc
  case head +: _ if Some(head) == stop => acc :+ head
  case head +:  tail  =>
    val adjacent = g.adjacent(head) match { case Some(ys) => ys case _ => Set.empty[A]}
    val xs = f(tail,(adjacent -- visited).toSeq)
    traverse0(xs, visited + head,acc :+ head)
}
/* If stop is not contained in the graph, full dfs is performed. */
if (g.contains(start)) traverse0(Seq(start),Set.empty,Seq.empty)
else Seq.empty[A]
}
```

## Usage

### dfs
```tut
import tiki._
import tiki.Predef._
import tiki.Traversal._
import scala.util.Random

val edges = Random.shuffle(List(Edge("A","B"),Edge("A","C"),Edge("B","D"),Edge("C","D")))
val search = dfs(AdjacencyList(edges),"A")
```

### bfs
```tut
import tiki._
import tiki.Predef._
import tiki.Traversal._
import scala.util.Random

val edges = Random.shuffle(List(Edge("A","B"),Edge("A","C"),Edge("B","D"),Edge("C","D")))
val search = bfs(AdjacencyList(edges),"A")
```
