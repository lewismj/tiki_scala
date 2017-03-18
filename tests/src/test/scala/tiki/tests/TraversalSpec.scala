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
import tiki.Traversal._

import scala.util.Random


class TraversalSpec extends TikiSuite with Checkers with Matchers with AllArbitrary {

  /*
    WIP: When the is an ArbitraryGraph, we can use ScalaCheck to verify
    the sequence order based on the rank of the vertices in the sequence.
    Therefore use a randomly generated DAG to correctly property check the
    traversals.

    WIP: Should use GraphRep generator to test against all available graph
    representations.
   */


  /* Sanity checks on depth-first and breadth-first searches. */

  test("`dfs` on missing vertex returns empty sequence") {
    val edges = Random.shuffle(List(Edge('A','B'),Edge('A','C'),Edge('B','D'),Edge('C','D')))
    val search = dfs(AdjacencyList(edges),'E')
    search should be (Seq.empty[Char])
  }

  test("`dfs` returns correct ordering for simple graph") {
    val edges = Random.shuffle(List(Edge('A','B'),Edge('A','C'),Edge('B','D'),Edge('C','D')))
    val search = dfs(AdjacencyList(edges),'A')
    val expected = Set(Seq('A','B','D','C'), Seq('A','C','D','B'))
    expected.contains(search) should be (true)
  }

  test("`bfs` returns correct ordering for simple graph") {
    val edges = Random.shuffle(List(Edge('A','B'),Edge('A','C'),Edge('B','D'),Edge('C','D')))
    val search = bfs(AdjacencyList(edges),'A')
    val expected = Set(Seq('A','C','B','D'),Seq('A','B','C','D'))
    expected.contains(search) should be (true)
  }

  test("`dfs` returns correct ordering for simple graph, and stops correctly.") {
    val edges = Random.shuffle(List(Edge('A','B'),Edge('A','C'),Edge('B','D'),Edge('C','D')))
    val search = dfs(AdjacencyList(edges),'A', Some('D'))
    val expected = Set(Seq('A','B','D'), Seq('A','C','D'))
    expected.contains(search) should be (true)
  }

  test("`bfs` returns correct ordering for simple graph, and stops correctly") {
    val edges = Random.shuffle(List(Edge('A','B'),Edge('A','C'),Edge('B','D'),Edge('C','D')))
    val search = bfs(AdjacencyList(edges),'A',Some('B'))
    val expected = Set(Seq('A','C','B'),Seq('A','B'))
    expected.contains(search) should be (true)
  }

}
