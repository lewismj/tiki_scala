---
layout: docs 
title:  "Utility"
section: "algorithms"
source: "core/src/main/scala/tiki/package.scala"
scaladoc: "#tiki"
---
# Utility functions

The package object defines various utility functions.

- `reverse(xs)` reverses an edge (of any type).
- `removeEdgeTo(g,xs)` remove all the edges to the _xs_ vertices of the _g_ graph.

## Usage


### reverse

```tut
    import tiki._
    import tiki.Predef._
    import tiki.implicits._
    
    List(1 --> 2, 1 --> 4, 2 --> 4).map(reverse)
    List('a' --> 'b' :+ "label.1", 'b' --> 'c' :+ "label.2").map(reverse)
```

### removeEdgeTo

This method is used to remove all edges in a graph that lead to a set of vertices.

```scala
  def removeEdgeTo[A](g: Digraph[A], xs: Stream[A]): Digraph[A] = new Digraph[A] {
    override def vertices: Stream[A] = g.vertices
    override def predecessors(v: A): Set[A] = if (xs.contains(v)) Set.empty[A] else g.predecessors(v)
    override def successors(v: A): Set[A] = g.successors(v).filter(xs.contains)
    override def contains(v: A): Boolean = g.contains(v)
    override def edges: Stream[EdgeLike[A]] = g.edges.filterNot(e => xs.contains(e.to))
  }
```