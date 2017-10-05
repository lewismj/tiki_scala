---
layout: docs 
title:  "Transpose"
section: "typeclasses"
source: "core/src/main/scala/tiki/Transpose.scala"
scaladoc: "#tiki.Transpose"
---
# Transpose

A class `Transpose` is used to define the interface for transposing a graph.

```scala
trait Transpose[T] {
  def transpose: T
}

object Transpose {
  def apply[T: Transpose]: Transpose[T] = implicitly
  def transpose[T: Transpose](t: T): T = Transpose[T].transpose
}
```
An example implementation is shown below. The transpose of the graph is a reversal of the edges,
this can be implemented independently of the underlying data structure (_e.g._an adjacency list) 
used to implement the particular graph interface.
```scala
  implicit class UnweightedT[T](g: Digraph[T]) extends Transpose[Digraph[T]] {
    override def transpose: Digraph[T] = new Digraph[T] {
      override def edges: Stream[Edge[T]] = g.edges.map(edge => Edge(edge.to,edge.from))
      override def predecessors(v: T): Stream[T] = g.successors(v)
      override def successors(v: T): Stream[T] = g.predecessors(v)
      override def contains(v: T): Boolean = g.contains(v)
      override def vertices: Stream[T] = g.vertices
    }
  }
```