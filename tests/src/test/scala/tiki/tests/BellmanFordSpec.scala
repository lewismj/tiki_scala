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

import scala.math._

/** WIP - need ScalaCheck test cases. */

class BellmanFordSpec extends TikiSuite with AllArbitrary {


  test("Bellman-Ford simple graph test .1") {

    val xs = Stream(
      'A' --> 'B' :# -1.0,
      'A' --> 'C' :# 4.0,
      'B' --> 'C' :# 3.0,
      'B' --> 'D' :# 2.0,
      'D' --> 'B' :# 1.0,
      'B' --> 'E' :# 2.0,
      'E' --> 'D' :# -3.0
    )

    val digraph = new WeightedDigraph[Char] {
      lazy val ys = AdjacencyList(xs)
      def contains(v: Char) = ys.contains(v)
      def vertices: Stream[Char] = ys.vertices
      def successors(v: Char) = ys.successors(v)
      def predecessors(v: Char) = ys.predecessors(v)
      def weights: Stream[WeightedEdge[Char]] = xs
    }

    val state = bellmanFord(digraph,'A')
    val expectedDistances = Map('A' -> 0.0, 'B' -> -1.0 , 'C' -> 2.0, 'D' -> - 2.0, 'E' -> 1.0)
    val expectedPredecessors = Map('B'->'A','C'->'B','D'->'E','E'->'B')

    state.distances should be(expectedDistances)
    state.predecessors should be(expectedPredecessors)
  }


  test("Can correctly find negative cycle.") {

    val xs = Stream(
      0 --> 1 :# 5.0,
      0 --> 2 :# 4.0,
      1 --> 3 :# 3.0,
      2 --> 1 :# -6.0,
      3 --> 2 :# 2.0
    )


    val digraph = new WeightedDigraph[Int] {
      lazy val ys = AdjacencyList(xs)
      def contains(v: Int) = ys.contains(v)
      def vertices: Stream[Int] = ys.vertices
      def successors(v: Int) = ys.successors(v)
      def predecessors(v: Int) = ys.predecessors(v)
      def weights: Stream[WeightedEdge[Int]] = xs
    }

    negativeCycle(digraph,0).isDefined should be (true)
  }

  test("Simple graph should have no negative cycles.") {
    val xs = Stream ('a' --> 'b' :# 5.0, 'b' --> 'c' :# 10.0, 'c' --> 'a' :# -5.0)
    val digraph = new WeightedDigraph[Char] {
      lazy val ys = AdjacencyList(xs)
      def contains(v: Char) = ys.contains(v)
      def vertices: Stream[Char] = ys.vertices
      def successors(v: Char) = ys.successors(v)
      def predecessors(v: Char) = ys.predecessors(v)
      def weights: Stream[WeightedEdge[Char]] = xs
    }

    val nCycles = negativeCycle(digraph,'a')
    nCycles.isDefined should be (false)
  }


  test("Simple arbitrage opportunity detected.") {

    val xs = Stream(
      "USD" --> "CAD" :# -log(1.005),
      "EUR" --> "USD" :# -log(1.349),
      "EUR" --> "GBP" :# -log(0.888),
      "EUR" --> "CHF" :# -log(1.433),
      "GBP" --> "CHF" :# -log(1.614),
      "GBP" --> "CAD" :# -log(1.538),
      "CHF" --> "USD" :# -log(0.942),
      "CHF" --> "EUR" :# -log(0.698),
      "GBP" --> "EUR" :# -log(1.126),
      "CAD" --> "GBP" :# -log(0.650),
      "CAD" --> "CHF" :# -log(1.049),
      "CAD" --> "USD" :# -log(0.995),
      "GBP" --> "CAD" :# -log(1.538),
      "CHF" --> "USD" :# -log(0.942),
      "CHF" --> "EUR" :# -log(0.698),
      "CHF" --> "GBP" :# -log(0.619),
      "CAD" --> "EUR" :# -log(0.732),
      "USD" --> "EUR" :# -log(0.741),
      "USD" --> "GBP" :# -log(0.657),
      "CHF" --> "GBP" :# -log(0.619),
      "CHF" --> "CAD" :# -log(0.953),
      "GBP" --> "USD" :# -log(1.521),
      "USD" --> "CHF" :# -log(1.061),
      "EUR" --> "CAD" :# -log(1.366)
    )


    val digraph = new WeightedDigraph[String] {
      lazy val ys = AdjacencyList(xs)
      def contains(v: String) = ys.contains(v)
      def vertices: Stream[String] = ys.vertices
      def successors(v: String) = ys.successors(v)
      def predecessors(v: String) = ys.predecessors(v)
      def weights: Stream[WeightedEdge[String]] = xs
    }

    // To test for arbitrage opportunities, you would map
    // over every currency looking for negative cycle.
    val cycle = negativeCycle(digraph, "USD")
    cycle.getOrElse(List.empty).toSet should equal(Set("USD","CAD","EUR"))
  }


}