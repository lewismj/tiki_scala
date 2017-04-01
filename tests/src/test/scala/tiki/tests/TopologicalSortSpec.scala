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
import tiki.Predef._
import tiki.implicits._
import tiki.Sort._



/** WIP - need ScalaCheck test cases. */
class TopologicalSortSpec extends TikiSuite with AllArbitrary {

  test("topological sort of graph with cycle should return none") {
    val xs = Stream(
      'A' --> 'B' :# 1.0,
      'B' --> 'C' :# 1.0 ,
      'C' --> 'A' :# 1.0
    )

    val adjacencyList = AdjacencyList(xs)
    val digraph = new WeightedDigraph[Char] {
      def contains(v: Char) = adjacencyList.contains(v)
      def vertices: Stream[Char] = adjacencyList.vertices
      def successors(v: Char) = adjacencyList.successors(v)
      def predecessors(v: Char) = adjacencyList.predecessors(v)
      def edges: Stream[WeightedEdge[Char]] = xs
    }

    hasCycle(digraph) should be (true)
  }

  test("`tsort` produces correct ordering of acyclic graph.") {

    val xs = Stream (
        'A' --> 'B',
        'A' --> 'C',
        'B' --> 'C',
        'B' --> 'D',
        'C' --> 'D',
        'D' --> 'E',
        'D' --> 'F',
        'E' --> 'F'
    )

    val adjacencyList = AdjacencyList(xs)
    val digraph = new Digraph[Char] {
      def contains(v: Char) = adjacencyList.contains(v)
      def vertices: Stream[Char] = adjacencyList.vertices
      def successors(v: Char) = adjacencyList.successors(v)
      def predecessors(v: Char) = adjacencyList.predecessors(v)
      def edges: Stream[Edge[Char]] = xs
    }

    val sorted = tsort(digraph).getOrElse(Stream.empty)
    sorted.mkString should be ("ABCDEF")
  }

}
