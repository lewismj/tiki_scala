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
import tiki.cluster._
import tiki.cluster.Point._
import tiki.cluster.Triangle._


class TriangleSpec extends TikiSuite with AllArbitrary {

  test("Collinear points should not form a triangle.") {
    val a = Point(0,0)
    val b = Point(1,0)
    val c = Point(2,0)
    maybeTriangle(a,b,c) should be (None)
  }

  test("Collinear points.") { (p1: Point, p2: Point, p3: Point) =>
    maybeTriangle(p1,p2,p3) match {
      case Some(_) => collinear(p1,p2,p3) should be (false)
      case _ => collinear(p1,p2,p3) should be (true)
    }
  }

  test("Circumcenter of triangle.") {
    val t0 = Triangle(Point(3.0, 2.0),Point(1.0, 4.0),Point(5.0,4.0))
    t0.u should be (Point(3.0,4.0))
    t0.ccContains(Point(t0.u.x+ε,t0.u.y+ε)) should be (true)
  }

  test("Approximately equal triangles.") { (p1: Point, p2: Point, p3: Point) =>
    val p4 = Point(p1.x+ (ε/2.0),p1.y+(ε/2.0))
    val p5 = Point(p2.x+ (ε/2.0),p2.y+(ε/2.0))
    val p6 = Point(p3.x+ (ε/2.0),p3.y+(ε/2.0))

    (Triangle(p1,p2,p3) ≅ Triangle(p4,p5,p6)) should be (true)
  }

  test("Generate edges from a triangle.") { (p1: Point, p2: Point, p3: Point) =>
    Triangle(p1,p2,p3).toEdges should be (List(p1 --> p2, p2 --> p3, p3 --> p1))
  }

  test("Shares vertex.") { (p1: Point, p2: Point, p3: Point, p4: Point) => {
    val t0 = Triangle(Point(3.0,2.0),Point(1.0, 4.0),Point(5.0,4.0))
    val t1 = Triangle(Point(2.0,3.0), Point(4.0,1.0),Point(4.0,5.0))
    t0.sharesVertex(t1) should be (false)
    /* Could permute 5 unique values etc.. */
    Triangle(p1,p2,p3).sharesVertex(Triangle(p1,p2,p4)) should be (true)
  }}

}
