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
import tiki.cluster._
import tiki.cluster.Point.{collinear,side}


class PointSpec extends TikiSuite with AllArbitrary {

  test("Add points.") { (p1: Point, p2: Point) => {
    val expected = tiki.cluster.Point(p1.x+p2.x,p2.x+p2.y)
    p1 +p2 should be (expected)
  }}

  test("Subtract points.") { (p1: Point, p2: Point) => {
    val expected = Point(p1.x-p2.x,p2.x-p2.y)
    p1 - p2 should be (expected)
  }}

  test("Cross product.") { (p1: Point, p2: Point) => {
    val expected = p1.x*p2.y - p1.y*p2.x
    p1 ⨯ p2 should be (expected)
  }}

  test("Dot product.") { (p1: Point, p2: Point) => {
    val expected = p1.x*p2.x + p1.y*p2.y
    p1 ⋅ p2 should be (expected)
  }}

  test("Approximately equal.") { (p1: Point) => {
    val p2 = Point(p1.x - (ε/2.0), p1.y - (ε/2.0))
    (p1 ≅ p2 ) should be (true)
  }}

  test("Correctly detect collinear points.") { (p1: Point) => {
    val p2 = Point(p1.x,p1.y+10)
    val p3 = Point(p1.x,p1.y-10)
    collinear(p1,p2,p3) should be (true)

    val p4 = Point(p1.x+10,p1.y)
    val p5 = Point(p1.x-10,p1.y)
    collinear(p1,p4,p5) should be (true)

    collinear(p1,Point(p1.x+10,p1.y+10),Point(p1.x-10,p1.y+10)) should be (false)
  }}

  test("Counter clockwise side is correct.") { (p1: Point, p2: Point, p3: Point)=> {
    side(p1,p2,p3) should be ((p2-p1) ⨯ (p3-p1))
  }}


}
