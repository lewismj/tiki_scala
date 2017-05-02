/*
 * Copyright (c) 2017
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package tiki
package instances

import tiki.geometry._

trait TexInstances  {

  /** todo: need to specialise the stream writing to properly place nodes in tikz picture. */
  implicit def texForInt: Tex[Int] = (i: Int) => s"%%$i;\n"
  implicit def texForChar: Tex[Char] = (ch: Char) => s"%%$ch;\n"
  implicit def texForStr: Tex[String] = (str: String) => s"%%$str;\n"
  implicit def texForDouble: Tex[Double] = (d: Double) => s"%%$d;\n"

  implicit def texForPoint[A]: Tex[Point]
    = (p: Point) =>  s"\t\\fill (${p.x},${p.y}) circle[radius=.5pt];\n"

  implicit def texForEdge[A]: Tex[Edge[A]] = (a: Edge[A]) => s"\t\\draw (${a.from})--(${a.to});\n"

  implicit def texForListOfA[A](implicit ev: Tex[A]): Tex[List[A]]
    = (a: List[A]) => a.iterator.map(ev.tex).mkString

  implicit def texForVectorOfA[A](implicit ev: Tex[A]): Tex[Vector[A]]
  = (a: Vector[A]) => a.iterator.map(ev.tex).mkString


  implicit def texForStreamOfA[A](implicit ev: Tex[A]): Tex[Stream[A]]
  = (a: Stream[A]) => a.iterator.map(ev.tex).mkString

  implicit def texForWeightedGraph[A](implicit ev: Tex[Stream[A]],
                                      ev1: Tex[Stream[WeightedEdge[A]]]): Tex[WeightedGraph[A]]
    = (g: WeightedGraph[A]) => ev.tex(g.vertices) ++ ev1.tex(g.weights)

  implicit def texForGraph[A](implicit ev: Tex[Stream[A]],
                                       ev1: Tex[Stream[Edge[A]]]): Tex[Graph[A]]
  = (g: Graph[A]) => ev.tex(g.vertices) ++ ev1.tex(g.edges)

}
