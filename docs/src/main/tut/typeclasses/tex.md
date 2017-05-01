---
layout: docs 
title:  "Tex"
section: "typeclasses"
source: "core/src/main/scala/tiki/Tex.scala"
scaladoc: "#tiki.Tex"
---
# Tex

Defines simple interface for producing LaTeX output.

```scala
trait Tex[A] {
  def tex(a: A): String
}

object Tex {
  def apply[T: Tex]: Tex[T] = implicitly
  
  def tex[T: Tex](t: T):String = Tex[T].tex(t)
  
  def toTikz[T: Tex](t: T): String = {
    val builder = new StringBuilder
    builder.append("""\documentclass[border=10pt]{standalone}""")
    builder.append("\n\\usepackage{tikz}")
    builder.append(
      """
        |\begin{document}
        |\begin{tikzpicture}
      """.stripMargin)

    builder.append(tex(t))

    builder.append(
      """
        |\end{tikzpicture}
        |\end{document}
      """.stripMargin)

    builder.toString
  }
}
```