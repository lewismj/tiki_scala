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
package geometry

import tiki.implicits._
import geometry.Point._

import scala.math._



/**
  * Triangle represented by three vertices. Used in the Delaunay Triangulation.
  */
case class Triangle(a: Point, b: Point, c: Point) {

  /** The a-offset circumcenter of the triangle. */
  private lazy val u0 = {
    val b0 = b - a
    val c0 = c - a
    val d0 = 2.0*(b0.x*c0.y - b0.y*c0.x)
    val bx = b0.x * b0.x
    val by = b0.y * b0.y
    val cx = c0.x * c0.x
    val cy = c0.y * c0.y
    val x = (c0.y*(bx + by) - b0.y*(cx + cy))/d0
    val y = (b0.x*(cx + cy) - c0.x*(bx + by))/d0
    Point(x,y)
  }

  /** The circumcenter of the triangle. */
  lazy val u = u0 + a

  /** Radius of circumcircle. */
  lazy val r2 = u0.x*u0.x + u0.y*u0.y
  lazy val r = sqrt(r2)

  /**
    * Returns true if the point given is within the circumcircle
    * of the triangle.
    *
    * @param p  the point.
    * @return true if point withing circumcircle, false otherwise.
    */
  def ccContains(p: Point): Boolean
  = (p.x-u.x)*(p.x-u.x) + (p.y-u.y)*(p.y-u.y) < r2

  /**
    * Returns true if given triangle has a matching vertex.
    *
    * @param that a triangle.
    * @return true if there is a matching vertex, false otherwise.
    */
  def sharesVertex(that: Triangle): Boolean = {
    def hasVertex(p: Point): Boolean = (p == this.a) || (p == this.b) || (p == this.c)
    hasVertex(that.a) || hasVertex(that.b) || hasVertex(that.c)
  }

  /**
    * Returns the vector of edges representing the triangle.
    *
    * @return the vector of edges.
    */
  def toEdges: Vector[Edge[Point]] = Vector(a-->b, b-->c, c-->a)
}

object Triangle {

  /**
    * Try to create a triangle, returns None if the points
    * specified are collinear.
    *
    * @param a  first point of triangle.
    * @param b  second point of triangle.
    * @param c  third point of triangle.
    * @return either a triangle or None, if points collinear.
    */
  def maybeTriangle(a: Point, b: Point, c: Point): Option[Triangle] =
    if (collinear(a,b,c)) None else Some(Triangle(a,b,c))
}