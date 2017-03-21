---
layout: docs 
title:  "Traversal"
section: "algorithms"
source: "core/src/main/scala/tiki/Traversal.scala"
scaladoc: "#tiki.Traversal"
---
# Traversal functions
 
Traversal is done by performing an `unfold` on the graph representation, the 
traversal function will return a stream of vertices.

```scala
private def unfold[T,R](z: T)(f: T => Option[(R,T)]): Stream[R] = f(z) match {
    case None => Stream.empty[R]
    case Some((r,v)) => r #:: unfold(v)(f)
}
```

The traversal can be depth or breadth first (_note_ the `distinct` on the stream does preserve order,
a vertex may be visited more than once in a traversal, most of the time we want the first time 
only).

```scala
private def traverse[A](g: DirectedGraphRep[A], v: A, dfs: Boolean): Stream[A] = {
    val traversal = unfold(List(v)) {
      case w :: Nil =>
        Some((w,g.successors(w).toList))
      case w :: vs =>
        val next = if (dfs) g.successors(w).toList ::: vs
        else vs ::: g.successors(w).toList
        Some((w,next))
      case _ =>
        None
    }
    traversal.distinct
}
```

 Two primary functions are available:
 
 - `dfs(g,start)` will perform a depth-first traversal of the graph _g_, starting at _start_ vertex.
 - `bfs(g,start)` as above, except a breadth first search will be done.
 
 Both are implemented in terms of the `unfold` and `traverse` functions:
 
 ```scala
private def visitOrder[A](g: DirectedGraphRep[A], start: A, dfs: Boolean): Stream[A]
  = if (g.contains(start)) traverse(g, start, dfs) else Stream.empty

def dfs[A](g: DirectedGraphRep[A], start: A): Stream[A]
  = visitOrder(g,start,dfs=true)

def bfs[A](g: DirectedGraphRep[A], start: A): Stream[A]
  = visitOrder(g,start,dfs=false)
```

```tut
import tiki._
import tiki.Predef._
import tiki.implicits._
import scala.util.Random
import tiki.Traversal._

val edges = Random.shuffle(List('A' --> 'B', 'A' --> 'C', 'B' --> 'D', 'C' --> 'D'))
val adj = buildAdjacencyList(edges)
val search = dfs(adj, 'A')
search.mkString

```