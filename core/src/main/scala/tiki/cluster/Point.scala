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

import spire.math._
import spire.implicits._


/**
  * Point represents 2D Cartesian coordinates.
  * No need to make generic, double makes sense for clustering etc.
  *
  * @param x the x coordinate.
  * @param y the y coordinate.
  */
case class Point(x: Real, y: Real) {

  /** Polar coordinates. */
  lazy val r  = r2.sqrt
  lazy val r2 = x * x + y * y
  lazy val φ  = atan2(y, x)

  /** +,- Point operators. */
  def +(that: Point): Point = Point(this.x + that.x, this.y + that.y)
  def -(that: Point): Point = Point(this.x - that.x, this.y - that.y)

  /** Cross and Dot Product. */
  def ⨯(that: Point): Real = this.x * that.y - this.y * that.x
  def ⋅(that: Point): Real = this.x * that.x + this.y * that.y
}


object Point {

  /**
    * Returns true, if point p3 is right of the p1, p2 line.
    *
    * @param p1 the first point.
    * @param p2 the second point.
    * @param p3 the third point.
    * @return true, if point p3 is left of the p1, p2 line,
    *         false otherwise.
    */
  def rightTurn(p1: Point, p2: Point, p3: Point): Boolean
    = 0.0 > ((p2 - p1) ⨯ (p3 - p2))


  /**
    * The distance between two points p1, p2.
    *
    * @param p1 the first point.
    * @param p2 the second point.
    * @return the distance between the points.
    */
  def distance(p1: Point, p2: Point): Real = (p1 - p2).r

  /**
    * Returns true if the points p1, p2 and p3 are collinear
    * (lie in a straight line).
    *
    * @param p1 the first point.
    * @param p2 the second point.
    * @param p3 the third point.
    * @return true, if the points are collinear, false otherwise.
    */
  def collinear(p1: Point, p2: Point, p3: Point): Boolean
    = ((p1-p2) ⨯ (p3-p2)) === 0.0


  /** !-- Counter clockwise utility functions. */
  def side(p1: Point, p2: Point, p3: Point): Real =  (p2-p1) ⨯ (p3-p1)
  def ccw(p1: Point, p2: Point, p3: Point): Int = side(p1, p2, p3) match {
    case s if s > 0 => 1
    case s if s < 0 => -1
    case _ => 0
  }

  /**
    * Calculate angle between two points p1, p2 (given the origin).
    *
    * @param origin the origin point.
    * @param p1 the first point.
    * @param p2 the second point.
    * @return the angle between the two points.
    */
  def angle(origin: Point, p1: Point, p2: Point): Real = {
    val d1 = p1 - origin
    val d2 = p2 - origin
    acos( (d1 ⋅ d2) / (d1.r * d2.r) )
  }

  def minAngle(p1: Point, p2: Point, p3: Point): Real =
    min(angle(p1,p2,p3),min(angle(p2,p3,p1),angle(p3,p1,p2)))

  /**
    * Ordering of `Point` by Y coordinate, if y's match, x's
    * are compared.
    */
  implicit object YXOrdering extends Ordering[Point] {
    /**
      * YX - comparison of the points p1, p2.
      *
      * @param p1 the first point.
      * @param p2 the second point.
      * @return 0 if the points are equal,
      *         -1 if y of first point < y of second point
      *         (if they match the x's are similarly compared).
      *         1 otherwise.
      */
    override def compare(p1: Point, p2: Point): Int = {
      if (p1 == p2) 0
      else if (p1.y < p2.y || (p1.y === p2.y && p1.x < p2.x)) -1
      else 1
    }
  }

  /**
    * Counter clockwise ordering for `Point`.
    *
    * @param origin the origin point.
    */
  implicit class CCWOrdering(origin: Point) extends Ordering[Point] {
    /**
      * Counter clockwise order of the points p1, p2.
      *
      * @param p1 the first point.
      * @param p2 the second point.
      * @return 0 if they are on the same line,
      *         -1 if p1 precedes p2,
      *         1 otherwise.
      */
    override def compare(p1: Point, p2: Point): Int = {
      val dy1 = p1.y - origin.y
      val dy2 = p2.y - origin.y
      if (dy1 >=0 && dy2 < 0) -1
      else if (dy2 >=0 && dy1 <0) 1
      else if ((dy1 === 0) && (dy2 === 0)) {
        val dx1 = p1.x - origin.x
        val dx2 = p2.x - origin.x
        if ( dx1 >= 0 && dx2 <0 ) -1
        else if (dx2 >= 0 && dx1 <0) 1
        else 0
      } else -ccw(origin,p1,p2) /* todo: avoid area re-calc. */
    }
  }
}
