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

  implicit def texForPoint[A]: Tex[Point]
    = (p: Point) =>  s"\t\\fill (${p.x},${p.y}) circle[radius=2pt] node [black,above=4] { };\n"

  implicit def texForListOfA[A](implicit ev: Tex[A]): Tex[List[A]]
    = (a: List[A]) => a.iterator.map(ev.tex).mkString

  implicit def texForStreamOfA[A](implicit ev: Tex[A]): Tex[Stream[A]]
  = (a: Stream[A]) => a.iterator.map(ev.tex).mkString

  implicit def texForWeightedGraph[A](implicit ev: Tex[Stream[A]],
                                      ev1: Tex[Stream[WeightedEdge[A]]]): Tex[WeightedGraph[A]]
    = (g: WeightedGraph[A]) => ev.tex(g.vertices) ++ ev1.tex(g.weights)


}
