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
package tests

import tiki.tests.arbitrary.AllArbitrary
import tiki.implicits._
import tiki.Path._

/** WIP: Need to add ScalaCheck tests. */

class KruskalSpec extends TikiSuite with AllArbitrary {

  test("Simple graph test, Kruskal finds minimum spanning tree.") {

    val xs = Stream(
      'A' --> 'B' :# 7.0,
      'A' --> 'D' :# 5.0,
      'B' --> 'C' :# 8.0,
      'B' --> 'E' :# 7.0,
      'C' --> 'E' :# 5.0,
      'D' --> 'B' :# 9.0,
      'D' --> 'E' :# 15.0,
      'D' --> 'F' :# 6.0,
      'E' --> 'F' :# 8.0,
      'E' --> 'G' :# 9.0,
      'F' --> 'G' :# 11.0
    )


    val graph = new WeightedUndirectedGraph[Char] {
      override def weights: Stream[WeightedEdge[Char]] = xs
      override def vertices: Stream[Char] = Stream('A', 'B', 'C', 'D', 'E', 'F', 'G')
    }

    val expected = Set(
      'A' --> 'D' :# 5.0,
      'C' --> 'E' :# 5.0,
      'D' --> 'F' :# 6.0,
      'A' --> 'B' :# 7.0,
      'B' --> 'E' :# 7.0,
      'E' --> 'G' :# 9.0
    )

    kruskal(graph).toSet should be(expected)


    val graph0 = new WeightedDigraph[Char] {
      lazy val ys = AdjacencyList(xs)
      override def weights: Stream[WeightedEdge[Char]] = xs
      override def vertices: Stream[Char] = Stream('A', 'B', 'C', 'D', 'E', 'F', 'G')
      override def contains(v: Char): Boolean = ys.contains(v)
      override def successors(v: Char): Set[Char] = ys.successors(v)
      override def predecessors(v: Char): Set[Char] = ys.predecessors(v)
    }

    kruskal(graph0).toSet should be (expected)
  }

}
