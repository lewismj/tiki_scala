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

import cats.Show
import tiki.geometry.Point


trait WeightedEdgeInstances {

  final class WeightDef[A](e: Edge[A]) {
    def :#(w: Double): WeightedEdge[A] = new WeightedEdge[A](e,w)
  }

  implicit def anyToWEdge[A](e: Edge[A]): WeightDef[A] = new WeightDef[A](e)

  implicit def weightedToEdge[A](s: Stream[WeightedEdge[A]]): Stream[Edge[A]] = s.map(_.edge)


  implicit def showForWEdge[A]: Show[WeightedEdge[A]]
    = (f: WeightedEdge[A]) => s"${f.from} --> ${f.to} :# ${f.weight}"

  implicit class WeightedEL[T](e: WeightedEdge[T]) extends EdgeLike[T] {
    override def from: T = e.edge.from
    override def to: T = e.edge.to
  }

  /*
   todo - need to look at angle of edges to determine correct placement of labels in tikz diagram.
  */
  implicit def texForWeightedEdgePoint: Tex[WeightedEdge[Point]] = (a: WeightedEdge[Point]) => {
    val edge =  s"\t\\draw (${a.from.x},${a.from.y}) --(${a.to.x},${a.to.y});\n"
    edge
  }
}


