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

```tut
    import tiki._
    import tiki.Predef._
    import tiki.implicits._
    
    List(1 --> 2, 1 --> 4, 2 --> 4).map(reverse)
    List('a' --> 'b' :+ "label.1", 'b' --> 'c' :+ "label.2").map(reverse)
```