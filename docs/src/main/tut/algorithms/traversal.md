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
```scala
 private def traverse[A](g: GraphRep[A], start: A, stop: Option[A])(f: S[A]): Seq[A] = {
    @tailrec
    def traverse0(remaining: Seq[A], visited: Set[A], acc: Seq[A]): Seq[A] = remaining match {
      case xs if xs.isEmpty => acc
      case _ if Some(remaining.head) == stop => acc :+ remaining.head
      case _ =>
        val xs = f(remaining.tail,(g.adjacent(remaining.head) -- visited).toSeq)
        traverse0(xs, visited + remaining.head,acc :+ remaining.head)
    }
    traverse0(Seq(start),Set.empty,Seq.empty)
  }

  def dfs[A](g: GraphRep[A], start: A, stop: Option[A] = None): Seq[A]
    = traverse(g,start,stop)((r,n) => n ++ r)

  def bfs[A](g: GraphRep[A], start: A, stop: Option[A] = None): Seq[A]
    = traverse(g,start,stop)((r,n) => r match {
      /* Avoid visiting final node twice, where last remaining node could also be 'next' */
      case _ if r.nonEmpty && r.last == n.head => r ++ n.tail
      case _ => r ++ n
    })
```

## Usage

### dfs
```tut
import tiki._
import tiki.Predef._
import scala.util.Random

val edges = Random.shuffle(List(Edge("A","B"),Edge("A","C"),Edge("B","D"),Edge("C","D")))
val search = dfs(AdjacencyList(edges),"A")
```

### bfs
```tut
import tiki._
import tiki.Predef._
import scala.util.Random

val edges = Random.shuffle(List(Edge("A","B"),Edge("A","C"),Edge("B","D"),Edge("C","D")))
val search = bfs(AdjacencyList(edges),"A")
```
