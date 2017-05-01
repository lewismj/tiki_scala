---
layout: docs 
title:  "Tex"
section: "typeclasses"
source: "core/src/main/scala/tiki/Tex.scala"
scaladoc: "#tiki.Tex"
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