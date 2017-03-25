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

import tiki.Predef._

/**
  * Path algorithms.
  */
object Path {

  /* Define +ve,-ve infinity. */
  val ∞ = Double.PositiveInfinity
  val ⧞ = Double.NegativeInfinity

  /**
    * Case class that represents the running state of the Bellman-Ford
    * algorithm.
    *
    * @param distances    current distances.
    * @param predecessors current predecessors.
    * @tparam A the vertex type.
    */
  case class PathState[A](distances: Map[A,Double], predecessors: Map[A,A]) {

    def update(key: A, distance: Double, predecessor: A): PathState[A] =
      PathState(distances.updated(key,distance),predecessors.updated(key,predecessor))

    def update(key: A, distance: Double): PathState[A] =
      PathState(distances.updated(key,distance),predecessors)
  }

  /** Companion object, provides initial empty state. */
  object PathState {
    def empty[A]: PathState[A] = PathState(Map.empty[A,Double],Map.empty[A,A])
  }

  /**
    * Bellman-Ford algorithm.
    *
    * @param g        a weighted digraph.
    * @param source   the source vertex.
    * @tparam A       the vertex type.
    * @return         the path state.
    */
  def bellmanFord[A](g: WeightedDigraph[A], source: A): PathState[A] = {
      Range(1,g.vertices.size).foldLeft(PathState.empty[A].update(source, 0))((xs, x) => {
      g.edges.foldLeft(xs)((ys, y) => {
        val (u, v, w) = (y.from, y.to, y.weight)
        val du = ys.distances.getOrElse(u, ∞)
        val dv = ys.distances.getOrElse(v, ∞)
        (du, dv, w) match {
          case _ if du + w < dv => ys.update(v, du + w, u)
          case _ => ys
        }
      })
    })
  }


  /**
    * Check to see if a negative cycle exists within a digraph.
    *
    * @param g        the digraph.
    * @param source   the source vertex.
    * @tparam A       the vertex type.
    * @return         a negative cycle, if one exists otherwise None.
    */
  def negativeCycle[A](g: WeightedDigraph[A], source: A): Option[Seq[A]] = {
    val state = bellmanFord(g,source)

    val maybeCycle = g.edges.flatMap(e=> {
      val (u,v,w) = (e.from,e.to,e.weight)
      if (state.distances.getOrElse(u,∞) + w < state.distances.getOrElse(v,⧞)) Some(v)
      else None
    })

    /* Return a negative cycle, if one exists. */
    maybeCycle.headOption.flatMap(v => {
      @tailrec
      def loop(v: A, cycle: Seq[A]) : Seq[A] = {
        val p = state.predecessors.getOrElse(v,v)
        if (cycle.contains(p)) cycle
        else {
          loop(p, cycle :+ p)
        }
      }
      Some(loop(v,Seq(v)))
    })
  }



}
