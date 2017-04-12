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
import tiki.Types._
import tiki.Zipper._


/**
  * Under development, all tests WIP,
  * will be replaced.
  */

class TreeSpec extends TikiSuite with AllArbitrary {

  /** simple test, move around example tree. */
  test("simple rose tree traversal") {

    /*
        0 -> [1,4,5]
        1 -> [2,3]
        4 -> []
        5 -> [6,7]

        i.e. similar to adjacency list.
     */
    val exampleTree = Node(0,
                        Stream(
                          Node(1,Stream(Node(2,Stream.empty),Node(3,Stream.empty))),
                          Node(4,Stream.empty),
                          Node(5,Stream(Node(6,Stream.empty),Node(7,Stream.empty)))))

    val z0 = Zipper(exampleTree,Stream.empty,Stream.empty,Stream.empty)
    z0.getLabel should be (0)

    z0.moveLeft should be (None)
    z0.moveRight should be (None)

    val z1 = z0.firstChild.getOrElse(z0)
    z1.getLabel should be (1)

    val z2 = z1.moveRight.getOrElse(z0)
    z2.getLabel should be(4)

    val z3 = z2.moveLeft.getOrElse(z0)
    z3.getLabel should be (1)

    val root = z3.root
    root should be (z0)

    root.fromZipper should be (exampleTree)

    val update = root.setLabel(-1)
    update.getLabel should be(-1)

    root.moveLeft should be (None)
    root.moveRight should be (None)

    val z4 = z0.lastChild.getOrElse(z0)
    z4.getLabel should be(5)
    
    val z5 = z4.moveLeft.getOrElse(z0)
    z5.getLabel should be (4)

    z5.firstChild should be (None)
    z5.lastChild should be (None)
  }

  test("levels correct for simple tree") {
    val t = Node(1,
              Stream(Node(3,Stream(Node(5,Stream.empty),Node(6,Stream.empty))),
                     Node(2,Stream.empty)))

    val expected = Stream(Stream(1),Stream(3,2),Stream(5,6))
    t.levels should be (expected)
  }

  test("flatten simple tree") {
    val t = Node(1,Stream(Node(2,Stream.empty), Node(3,Stream.empty)))
    t.flatten.mkString should be ("123")
    t.rootLabel should be (1)
    t.subForest.map(_.rootLabel).toList.mkString should be ("23")
  }

  test("map node") {
    val t = Node(1, Stream( Node(3, Stream(Node(5,Stream.empty),
        Node(6,Stream.empty))), Node(2,Stream.empty)))
    val t2 = t.map(_ + 1)

    val expected = Stream(Stream(2),Stream(4,3),Stream(6,7))
    t2.levels should be (expected)
  }

}
