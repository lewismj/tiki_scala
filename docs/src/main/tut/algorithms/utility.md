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

### reverse

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/reverse.png)


```tut
    import tiki._
    import tiki.Predef._
    import tiki.implicits._
    
    List('a' --> 'b', 'b' --> 'c', 'c' --> 'd').map(reverse)
```