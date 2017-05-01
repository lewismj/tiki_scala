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

import tiki.implicits._
import tiki.geometry._


class TriangulationSpec extends TikiSuite {

  test("Delaunay triangulation test.1") {
    val points = Vector(
      Point(3, 3),
      Point(3, 5),
      Point(0, 1),
      Point(2, 5),
      Point(-2, 2),
      Point(-3, 2),
      Point(6, 5),
      Point(-3, 4),
      Point(-5, 2),
      Point(-5, -1),
      Point(1, -2),
      Point(-3, -2),
      Point(4, 2),
      Point(5, 1),
      Point(-5, 1),
      Point(3, -2),
      Point(0, 5),
      Point(0, 0),
      Point(7, 4))

    val expectedEdges =
      Set(
        Point(5.0,1.0) --> Point(4.0,2.0),
        Point(4.0,2.0) --> Point(7.0,4.0),
        Point(7.0,4.0) --> Point(5.0,1.0),
        Point(6.0,5.0) --> Point(4.0,2.0),
        Point(7.0,4.0) --> Point(6.0,5.0),
        Point(-3.0,-2.0) --> Point(1.0,-2.0),
        Point(1.0,-2.0) --> Point(0.0,0.0),
        Point(0.0,0.0) --> Point(-3.0,-2.0),
        Point(-2.0,2.0) --> Point(0.0,1.0),
        Point(0.0,1.0) --> Point(0.0,0.0),
        Point(0.0,0.0) --> Point(-2.0,2.0),
        Point(4.0,2.0) --> Point(0.0,1.0),
        Point(0.0,0.0) --> Point(4.0,2.0),
        Point(4.0,2.0) --> Point(3.0,-2.0),
        Point(3.0,-2.0) --> Point(0.0,0.0),
        Point(-3.0,-2.0) --> Point(-2.0,2.0),
        Point(-2.0,2.0) --> Point(0.0,0.0),
        Point(3.0,-2.0) --> Point(1.0,-2.0),
        Point(0.0,0.0) --> Point(3.0,-2.0),
        Point(2.0,5.0) --> Point(3.0,3.0),
        Point(3.0,3.0) --> Point(0.0,5.0),
        Point(0.0,5.0) --> Point(2.0,5.0),
        Point(-3.0,4.0) --> Point(-2.0,2.0),
        Point(-2.0,2.0) --> Point(0.0,5.0),
        Point(0.0,5.0) --> Point(-3.0,4.0),
        Point(0.0,1.0) --> Point(0.0,5.0),
        Point(0.0,5.0) --> Point(-2.0,2.0),
        Point(3.0,3.0) --> Point(0.0,1.0),
        Point(0.0,5.0) --> Point(3.0,3.0),
        Point(3.0,-2.0) --> Point(5.0,1.0),
        Point(-5.0,2.0) --> Point(-3.0,2.0),
        Point(-3.0,2.0) --> Point(-5.0,1.0),
        Point(-5.0,1.0) --> Point(-5.0,2.0),
        Point(-5.0,-1.0) --> Point(-3.0,2.0),
        Point(-5.0,1.0) --> Point(-5.0,-1.0),
        Point(6.0,5.0) --> Point(3.0,3.0),
        Point(3.0,3.0) --> Point(4.0,2.0),
        Point(4.0,2.0) --> Point(6.0,5.0),
        Point(0.0,1.0) --> Point(4.0,2.0),
        Point(4.0,2.0) --> Point(3.0,3.0),
        Point(-3.0,2.0) --> Point(-2.0,2.0),
        Point(-2.0,2.0) --> Point(-3.0,-2.0),
        Point(-3.0,-2.0) --> Point(-3.0,2.0),
        Point(-3.0,2.0) --> Point(-3.0,-2.0),
        Point(-3.0,-2.0) --> Point(-5.0,-1.0),
        Point(-3.0,4.0) --> Point(-3.0,2.0),
        Point(-3.0,2.0) --> Point(-5.0,2.0),
        Point(-5.0,2.0) --> Point(-3.0,4.0),
        Point(-2.0,2.0) --> Point(-3.0,4.0),
        Point(3.0,5.0) --> Point(3.0,3.0),
        Point(3.0,3.0) --> Point(6.0,5.0),
        Point(6.0,5.0) --> Point(3.0,5.0),
        Point(3.0,3.0) --> Point(2.0,5.0),
        Point(2.0,5.0) --> Point(3.0,5.0))

    val t = Triangulation.bowyerWatson(points)
    t.toSet should be (expectedEdges)
  }
}
