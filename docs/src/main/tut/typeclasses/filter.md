---
layout: docs 
title:  "Filter"
section: "typeclasses"
source: "core/src/main/scala/tiki/Filter.scala"
scaladoc: "#tiki.Filter"
---
# Filter

Utility rather than type class, defines the behaviour for pruning 
graph vertices.

## Functions

- `filterNot(l)` remove the list of `l` vertices from any graph.

```scala
trait Filter[A,B] {
  def filterNot(l: List[A]): B
}

object Filter {
  def filterNot[A,B](l: List[A])(implicit t: Filter[A,B]): B = t.filterNot(l)
}
```