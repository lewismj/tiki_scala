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

import tiki.Predef._
import org.scalatest.Matchers
import org.scalatest.prop.Checkers
import tiki.{Edge, LEdge}
import tiki.tests.arbitrary.AllArbitrary
import tiki.implicits._

class EdgeSpec  extends TikiSuite with Checkers with Matchers with AllArbitrary {

  test("Edge creates correct to and from vertices") { (x: Int, y: Int) => {
    (x --> y) should have ('from (x), 'to (y))
  }}

  test("LEdge creates correct to, from vertices and label") { (x: Int, y: Int, z: Double) => {
    (x --> y :+ z) should have ('from (x), 'to (y), 'label (z))
  }}

  test("`lmap` correctly relabels a labelled edge") { (x: Int, y: Int, w: Double, s: String) => {
    (x --> y :+ w).lmap[String](_=>s) should have ('label (s), 'from (x), 'to (y))
  }}

  /* Force path for scoverage/codecov. */
  test("can implicitly create an edge") {
    val e = 1 --> 2
    e should be (Edge[Int](1,2))
  }

  test("`lmap` correctly relabels.") {
    val l0 = LEdge[Int,Double](Edge(1,2),1.0)
    val l1 = l0.lmap[String](_.toString)
    l1.label should be(1.0.toString)
  }

  test("Labelled edge properties are correct.") {
    val l0 = LEdge[Int,Double](Edge(1,2),1.0)
    val (from,to,label) = (l0.from,l0.to,l0.label)
    (from, to, label) should be ((1,2,1.0))
  }

}
