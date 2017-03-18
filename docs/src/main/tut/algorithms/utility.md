---
layout: docs 
title:  "Utility"
section: "algorithms"
source: "core/src/main/scala/tiki/package.scala"
scaladoc: "#tiki"
---
# Utility functions

The package object defines various utility functions.

- `reverse(xs)` reverses an edge.

## Usage


### reverse

Implemented using the `map` function on the edge.

```scala
def reverse[A](e: EdgeLike[A]): Edge[A] = Edge(e.to,e.from)
```

```tut
import tiki._
import tiki.Predef._

List(Edge(1,2),Edge(1,4),Edge(2,4)).map(reverse)
```