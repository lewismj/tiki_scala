---
layout: docs 
title:  "Traversal"
section: "algorithms"
source: "core/src/main/scala/tiki/Traversal.scala"
scaladoc: "#tiki.Traversal"
---
# Traversal Functions
 
Traversal is done by performing an `unfold` on the graph representation, the 
 function will return a stream of vertices.
```scala
private def unfold[T,R](z:T)(f: T => Option[(R,T)]): Trampoline[Stream[R]] = f(z) match {
    case None => Trampoline.done(Stream.empty[R])
    case Some((r,v)) =>
        Trampoline.suspend(unfold(v)(f)).flatMap(stream => Trampoline.done(r #:: stream))
  }
}
```
The traversal can be depth or breadth first. _Note_ the `distinct` on the stream (_visitOrder_ function) 
does preserve order. 
A vertex may be visited more than once in a traversal, most of the time we want the first instance.

Currently, cycles are ignored (i.e. the stream _won't_ loop infinitely.)
```scala
private def traverse[A](g: Digraph[A], v: A, dfs: Boolean): Stream[A]
= unfold( (List(v),Set.empty[A]) ) {
      case (current,visited) => current match {
        case w :: Nil =>
          Some((w, (g.successors(w).toList.filterNot(visited.contains), visited + w)))
        case w :: vs =>
          val next = if (dfs) g.successors(w).toList ::: vs
          else vs ::: g.successors(w).toList
          Some((w, (next.filterNot(visited.contains), visited + w)))
        case _ =>
          None
      }
    }.run
```
 Two primary functions are available:
 
 - `dfs(g,start)` will perform a depth-first traversal of the graph _g_, starting at _start_ vertex.
 - `bfs(g,start)` as above, except a breadth first search will be done.
 
 Both are implemented in terms of the `visitOrder` function.
 ```scala
private def visitOrder[A](g: Digraph[A], start: A, dfs: Boolean): Stream[A]
  = if (g.contains(start)) traverse(g, start, dfs).distinct else Stream.empty

def dfs[A](g: Digraph[A], start: A): Stream[A]
  = visitOrder(g,start,dfs=true)

def bfs[A](g: Digraph[A], start: A): Stream[A]
  = visitOrder(g,start,dfs=false)
```

```tut
import tiki._
import tiki.Predef._
import tiki.Traversal._
import tiki.implicits._
import scala.util.Random


val edges = Stream('A' --> 'B', 'A' --> 'C', 'B' --> 'D', 'C' --> 'D')
val adj = AdjacencyList(edges)
val search = dfs(adj, 'A')
search.mkString
```