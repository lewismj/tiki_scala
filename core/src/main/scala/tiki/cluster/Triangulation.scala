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
package cluster

import tiki.implicits._
import spire.math.Real
import spire.implicits._


object Triangulation {

  /* constant used to create boundary triangle. */
  val k = 2

  /** possible use composable fold if available in cats? */
  case class MinMax(xMin: Real, xMax: Real, yMin: Real, yMax: Real)
  def minMax(xs: Vector[Point]): MinMax = {
    xs.foldLeft(MinMax(∞,⧞,∞,⧞))((acc,pt)=> {
      val xMin = if (pt.x < acc.xMin) pt.x else acc.xMin
      val xMax = if (pt.x > acc.xMax) pt.x else acc.xMax
      val yMin = if (pt.y < acc.yMin) pt.y else acc.yMin
      val yMax = if (pt.y > acc.yMax) pt.y else acc.yMax
      MinMax(xMin,xMax,yMin,yMax)
    })
  }

  /**
    * Creates a super triangle given min,max boundary.
    *
    * @param minMax the min, max boundary.
    * @return a triangle covering the area of the boundary.
    */
  def superTriangle(minMax: MinMax): Triangle = minMax match {
    case MinMax(xMin,xMax,yMin,yMax) =>
      val dx = xMax - xMin
      val dy = yMax - yMin
      val xMid = (xMin + xMax)/2.0
      val yMid = (yMin + yMax)/2.0
      val dMax = dx.max(dy)
      Triangle(
        Point(xMid - k * dMax, yMid - dMax),
        Point(xMid, yMid + k * dMax),
        Point(xMid + k * dMax, yMid - dMax))
  }

  /**
    * Implementation of Bowyer-Watson algorithm for Delaunay triangulation.
    *
    * @param points the vector of 2D points.
    * @return the vector of edges.
    */
  def bowyerWatson(points: Vector[Point]): Vector[Edge[Point]] = {

    case class BadTriangles(triangles: Vector[Triangle], edges: Vector[Edge[Point]])
    val boundary = superTriangle(minMax(points))

    val triangles = points.foldLeft(List(boundary))((ts, pt)=> {

      val init = BadTriangles(Vector.empty[Triangle],Vector.empty[Edge[Point]])
      val BadTriangles(xs,ys) = ts.foldLeft(init)((bt, t) => {
        if (t.ccContains(pt))
          BadTriangles(t +: bt.triangles, Vector(t.a --> t.b, t.b --> t.c, t.c --> t.a) ++ bt.edges)
        else bt
      })

      val polygon = ys.groupBy(e=> if (e.from.r2 > e.to.r2) e else e.to --> e.from)
                    .filterNot(_._2.size>1).keys

      polygon.foldLeft(ts.diff(xs))((zs,edge) => Triangle(edge.from,edge.to,pt) :: zs)
    }).filterNot(_.sharesVertex(boundary))

    triangles.map(_.toEdges).flatten.distinct.toVector
  }

}
