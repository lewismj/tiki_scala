---
layout: docs 
title:  "Transposable"
section: "typeclasses"
source: "core/src/main/scala/tiki/Transposable.scala"
scaladoc: "#tiki.Transposable"
---
# Transposable

A simple type class `Transposable` is used to define the interface for transposing a graph.

```scala
trait Transposable[T] {
  def transpose: T
}

object Transposable {
  def apply[T: Transposable]: Transposable[T] = implicitly
}
```

Within the `implicits` section:

```scala
def transpose[T: Transposable](t: T): T= Transposable[T].transpose
 ```
 
 
An example implementation is shown below. The transpose of the graph is a reversal of the edges,
this can be implemented independently of the underlying representation used to implement the particular graph
interface.

```scala
implicit class Weighted[T](g: WeightedDigraph[T]) extends Transposable[WeightedDigraph[T]] {
  override def transpose: WeightedDigraph[T] = new WeightedDigraph[T] {
      override def edges: Stream[WeightedEdge[T]] = g.edges.map(edge=>tiki.reverse(edge))
      override def predecessors(v: T): Set[T] = g.successors(v)
      override def successors(v: T): Set[T] = g.predecessors(v)
      override def contains(v: T): Boolean = g.contains(v)
      override def vertices: Stream[T] = g.vertices
  }
}
```