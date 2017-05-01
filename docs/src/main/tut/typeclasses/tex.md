---
layout: docs 
title:  "Filter"
section: "typeclasses"
source: "core/src/main/scala/tiki/Filter.scala"
scaladoc: "#tiki.Filter"
---
# Tex

Defines simple interface for producing LaTeX output. Use by the `TexWriter` functions.

```scala
trait Tex[A] {
  def tex(a: A): String
}

object Tex {
  def apply[T: Tex]: Tex[T] = implicitly
  def tex[T: Tex](t: T):String = Tex[T].tex(t)
}
```