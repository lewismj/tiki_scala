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
package tiki.tests

import org.scalatest.Matchers
import org.scalatest.prop.Checkers
import tiki.Predef._
import tiki._
import tiki.implicits._
import tiki.tests.arbitrary.AllArbitrary

class TikiSpec extends TikiSuite with Checkers with Matchers with AllArbitrary {

  test("`reverse` of an edge should swap the two and from vertices") { (x:Int, y:Int) => {
     reverse(x --> y) should have ('from (y), 'to (x))
  }}

  test("`reverse` of a labelled edge should swap the vertices") { (x: Int, y: Int, z: Double) => {
    reverse(x --> y :+ z)  should have ('from (y), 'to (x), 'label (z))
  }}

  test("`removeEdgeTo` correctly removes edges from a weighted graph.") {
    val xs = Stream(
      'A' --> 'B' :# -1.0,
      'A' --> 'C' :# 4.0,
      'B' --> 'C' :# 3.0,
      'B' --> 'D' :# 2.0,
      'D' --> 'B' :# 1.0,
      'B' --> 'E' :# 2.0,
      'E' --> 'D' :# -3.0
    )

    val adjacencyList = AdjacencyList(xs)

    val digraph = new WeightedDigraph[Char] {
      def contains(v: Char) = adjacencyList.contains(v)
      def vertices: Stream[Char] = adjacencyList.vertices
      def successors(v: Char) = adjacencyList.successors(v)
      def predecessors(v: Char) = adjacencyList.predecessors(v)
      override def edges: Stream[WeightedEdge[Char]] = xs
    }

    val updated = removeEdgeTo(digraph,Stream('B','C'))

    val pb  = updated.predecessors('B')
    val pc  = updated.predecessors('C')
    val edgeTo = updated.edges.filter(e=>e.to == 'B' || e.to == 'C').map(_.from).toSet
    pb.union(pc).union(edgeTo) should be (Set.empty)
  }

  test("`removeEdgeTo` correctly removes edges from an un-weighted graph.") {
    val xs = Stream(
      'A' --> 'B' ,
      'A' --> 'C' ,
      'B' --> 'C' ,
      'B' --> 'D' ,
      'D' --> 'B' ,
      'B' --> 'E' ,
      'E' --> 'D'
    )

    val adjacencyList = AdjacencyList(xs)

    val digraph = new Digraph[Char] {
      def contains(v: Char) = adjacencyList.contains(v)
      def vertices: Stream[Char] = adjacencyList.vertices
      def successors(v: Char) = adjacencyList.successors(v)
      def predecessors(v: Char) = adjacencyList.predecessors(v)
      override def edges: Stream[Edge[Char]] = xs
    }

    val updated = removeEdgeTo(digraph,Stream('B','C'))

    val pb  = updated.predecessors('B')
    val pc  = updated.predecessors('C')
    val edgeTo = updated.edges.filter(e=>e.to == 'B' || e.to == 'C').map(_.from).toSet
    pb.union(pc).union(edgeTo) should be (Set.empty)
  }


}
