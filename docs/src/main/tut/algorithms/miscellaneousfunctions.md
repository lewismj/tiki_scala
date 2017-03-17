---
layout: docs 
title:  "MiscellaneousFunctions"
section: "datastructures"
source: "core/src/main/scala/tiki/package.scala"
scaladoc: "#tiki"
---
## Miscellaneous functions

The package object defines various utility functions.

- reverseAll(_xs_) reverses an iterable collection of edges.

## Usage

### reverseAll

```tut
import tiki._
import tiki.Predef._

val edges = List(Edge(1,2),Edge(1,4),Edge(2,4))
val reverse = reverseAll(edges)
```