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
import tiki.Components._


/* WIP: initial check-in Kosaraju's Algorithm */
class KosarajuSpec extends TikiSuite with AllArbitrary {

  test("strongly connected components .1") {
    val xs =  Stream(
      1 --> 0,
      0 --> 2,
      2 --> 1,
      0 --> 3,
      3 --> 4
    )

    val adj = AdjacencyList(xs)
    val g = new Digraph[Int] {
      override def contains(v: Int): Boolean = adj.contains(v)
      override def successors(v: Int): Set[Int] = adj.successors(v)
      override def predecessors(v: Int): Set[Int] = adj.predecessors(v)
      override def vertices: Stream[Int] = adj.vertices
      override def edges: Stream[EdgeLike[Int]] = xs
    }

    //val expected = Set(List(4), List(3), List(0, 1, 2))
    //kosaraju(g).toSet should be (expected)
  }

  test("strongly connect components .2") {
    val xs = Stream(
      1 --> 2,
      2 --> 3,
      2 --> 4,
      2 --> 5,
      3 --> 6,
      4 --> 5,
      4 --> 7,
      5 --> 2,
      5 --> 6,
      5 --> 7,
      6 --> 3,
      6 --> 8,
      7 --> 8,
      7 --> 10,
      8 --> 7,
      9 --> 7,
      10 --> 9,
      10 --> 11,
      11 --> 12,
      12 --> 10
    )

    val adj = AdjacencyList(xs)
    val g = new Digraph[Int] {
      override def contains(v: Int): Boolean = adj.contains(v)
      override def successors(v: Int): Set[Int] = adj.successors(v)
      override def predecessors(v: Int): Set[Int] = adj.predecessors(v)
      override def vertices: Stream[Int] = adj.vertices
      override def edges: Stream[EdgeLike[Int]] = xs
    }
    import scala.Predef._
    println(kosaraju(g))

  }

}
