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


import org.scalatest.Matchers
import org.scalatest.prop.Checkers
import tiki.tests.arbitrary.AllArbitrary
import tiki.Predef._
import tiki.implicits._
import tiki.Path._

import scala.math._

/** WIP - need ScalaCheck test cases. */

class BellmanFordSpec extends TikiSuite with Checkers with Matchers with AllArbitrary {


  test("Bellman-Ford simple graph test .1") {

    val xs = List(
      'A' --> 'B' :# -1.0,
      'A' --> 'C' :# 4.0,
      'B' --> 'C' :# 3.0,
      'B' --> 'D' :# 2.0,
      'D' --> 'B' :# 1.0,
      'B' --> 'E' :# 2.0,
      'E' --> 'D' :# -3.0
    )
    val adjacencyList = buildAdjacencyList(xs)


    val digraph = new WeightedDigraph[Char] {
      /* Use adjacency list for basic digraph implementation. */
      def contains(v: Char) = adjacencyList.contains(v)
      def vertices: Stream[Char] = adjacencyList.vertices
      def successors(v: Char) = adjacencyList.successors(v)
      def predecessors(v: Char) = adjacencyList.predecessors(v)
      /* adjacency doesn't store edges. */
      def edges: Stream[WeightedEdge[Char]] = xs.toStream
    }

    val state = bellmanFord(digraph,'A')
    val expected = Map('A' -> 0.0, 'B' -> -1.0 , 'C' -> 2.0, 'D' -> - 2.0, 'E' -> 1.0)

    state.distances should be(expected)
  }


  test("Negative cycle found") {

    val xs = List(
      0 --> 1 :# 5.0,
      0 --> 2 :# 4.0,
      1 --> 3 :# 3.0,
      2 --> 1 :# -6.0,
      3 --> 2 :# 2.0
    )
    val adjacencyList = buildAdjacencyList(xs)

    val digraph = new WeightedDigraph[Int] {
      /* Use adjacency list for basic digraph implementation. */
      def contains(v: Int) = adjacencyList.contains(v)
      def vertices: Stream[Int] = adjacencyList.vertices
      def successors(v: Int) = adjacencyList.successors(v)
      def predecessors(v: Int) = adjacencyList.predecessors(v)
      /* adjacency doesn't store edges. */
      def edges: Stream[WeightedEdge[Int]] = xs.toStream
    }


    val nCycles = negativeCycle(digraph,0)
    nCycles.size should be (1)
  }


  test("simple graph should have no negative cycles") {
    val xs = List ('a' --> 'b' :# 5.0, 'b' --> 'c' :# 10.0, 'c' --> 'a' :# -5.0)
    val adjacencyList = buildAdjacencyList(xs)
    val digraph = new WeightedDigraph[Char] {
      /* Use adjacency list for basic digraph implementation. */
      def contains(v: Char) = adjacencyList.contains(v)
      def vertices: Stream[Char] = adjacencyList.vertices
      def successors(v: Char) = adjacencyList.successors(v)
      def predecessors(v: Char) = adjacencyList.predecessors(v)
      /* adjacency doesn't store edges. */
      def edges: Stream[WeightedEdge[Char]] = xs.toStream
    }

    val nCycles = negativeCycle(digraph,'a')
    nCycles.isEmpty should be (true)
  }


  test("simple arbitrage detection") {

    val xs = List(
      "CAD" --> "USD" :# -1.0 * log(0.995),
      "CAD" --> "EUR" :# -1.0 * log(0.732),
      "CAD" --> "GBP" :# -1.0 * log(0.650),
      "CAD" --> "CHF" :# -1.0 * log(1.049),
      "GBP" --> "USD" :# -1.0 * log(1.521),
      "GBP" --> "EUR" :# -1.0 * log(1.126),
      "GBP" --> "CHF" :# -1.0 * log(1.614),
      "GBP" --> "CAD" :# -1.0 * log(1.538),
      "USD" --> "EUR" :# -1.0 * log(0.741),
      "USD" --> "GBP" :# -1.0 * log(0.657),
      "USD" --> "CHF" :# -1.0 * log(1.061),
      "USD" --> "CAD" :# -1.0 * log(1.005),
      "CHF" --> "USD" :# -1.0 * log(0.942),
      "CHF" --> "EUR" :# -1.0 * log(0.698),
      "CHF" --> "GBP" :# -1.0 * log(0.619),
      "CHF" --> "CAD" :# -1.0 * log(0.953),
      "EUR" --> "USD" :# -1.0 * log(1.349),
      "EUR" --> "GBP" :# -1.0 * log(0.888),
      "EUR" --> "CHF" :# -1.0 * log(1.433),
      "EUR" --> "CAD" :# -1.0 * log(1.366)
    )

    val adjacencyList = buildAdjacencyList(xs)

    val digraph = new WeightedDigraph[String] {
      /* Use adjacency list for basic digraph implementation. */
      def contains(v: String) = adjacencyList.contains(v)

      def vertices: Stream[String] = adjacencyList.vertices

      def successors(v: String) = adjacencyList.successors(v)

      def predecessors(v: String) = adjacencyList.predecessors(v)

      /* adjacency doesn't store edges. */
      def edges: Stream[WeightedEdge[String]] = xs.toStream
    }

    // To test for arbitrage opportunities, you would map
    // over every currency looking for negative cycle.
    val cycle = negativeCycle(digraph, "USD")
    cycle.isEmpty should be (false)

   import scala.Predef._
    println(cycle)
   // val trades = ("USD" :: cycle).iterator.sliding(2).toList
   // println(trades)



  }

}