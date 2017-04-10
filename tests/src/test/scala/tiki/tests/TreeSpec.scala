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
import tiki.data.Tree.{Leaf, Node}


/**
  * Under development, all tests WIP,
  * will be replaced.
  */

class TreeSpec extends TikiSuite with AllArbitrary {


  test("wip: flatten simple tree") {
    val t = Node(1,Stream(Leaf(2), Leaf(3)))
    t.flatten.mkString should be ("123")
    t.rootLabel should be (1)
    t.subForest.map(_.rootLabel).toList.mkString should be ("23")
  }

  test("wip: levels correct for simple tree") {
    val t = Node(1, Stream( Node(3, Stream(Leaf(5),Leaf(6))), Leaf(2)))
    val expected = Stream(Stream(1),Stream(3,2),Stream(5,6))
    t.levels should be (expected)
  }

  test("wip: map node") {
    val t = Node(1, Stream( Node(3, Stream(Leaf(5),Leaf(6))), Leaf(2)))
    val t2 = t.map(_ + 1)

    val expected = Stream(Stream(2),Stream(4,3),Stream(6,7))
    t2.levels should be (expected)
  }

}
