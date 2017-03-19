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

## Usage


### reverse

```scala
/**
 * Provides 'reverse' function for different 'Edge' case classes.
 * `Edge` classes shouldn't form an inheritance hierarchy.
 */
object reverse extends Poly1 {
  implicit def edgeLike[A] : Case.Aux[EdgeLike[A],Edge[A]]= at({x=> x.to --> x.from})
  implicit def edge[A] : Case.Aux[Edge[A],Edge[A]]= at({x=> x.to --> x.from})
  implicit def labelledEdge[A,B] : Case.Aux[LEdge[A,B],LEdge[A,B]]= at({x=> x.edge.to --> x.edge.from :+ x.label})
}
```

```tut
import tiki._
import tiki.Predef._
import tiki.implicits._

List(1 --> 2, 1 --> 4, 2 --> 4).map(reverse)
List('a' --> 'b' :+ 1.1, 'b' --> 'c' :+ 2.2).map(reverse)
```