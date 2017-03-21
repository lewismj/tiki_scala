---
layout: docs 
title:  "GraphRep"
section: "datastructures"
source: "core/src/main/scala/tiki/GraphRep.scala"
scaladoc: "#tiki.GraphRep"
---
# GraphRep

## Traits

`GraphRep` defines a trait that different graph representations should adhere to.

- `contains(v)` should return true if the vertex belongs in the graph.

```scala
trait GraphRep[A] {
  def contains(v: A): Boolean
}
```

`DirectedGraphRep` extends the `GraphRep` and defined the functions that directed graphs
should implement.

- `successors(v)` the vertices that have an incoming edge *from* _v_.
- `predecessora(v)` the vertices that have an outgoing edge *to* _v_.

```scala
trait DirectedGraphRep[A] extends GraphRep[A] {
  def contains(v: A): Boolean
  def successors(v: A): Option[Set[A]]
  def predecessors(v: A): Option[Set[A]]
}
```
