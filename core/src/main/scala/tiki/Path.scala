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

  val ∞ = Double.MaxValue

  /**
    * Case class that represents the running state of the Bellman-Ford
    * algorithm.
    *
    * @param distances    current distances.
    * @param predecessors current predecessors.
    * @tparam A the vertex type.
    */
  case class PathState[A](distances: Map[A,Double], predecessors: Map[A,A])


  object PathState {
    def apply[A](source: A): PathState[A] =
      PathState(Map.empty[A,Double].updated(source,0.0),Map.empty[A,A])
  }


  private def relaxEdge[A](state: PathState[A], e: WeightedEdge[A]): PathState[A] = {
    val du = state.distances.getOrElse(e.from, ∞)
    val dv = state.distances.getOrElse(e.to, ∞)

    if (du + e.weight  < dv ) {
      val distances = state.distances.updated(e.to,du + e.weight)
      val predecessors = state.predecessors.updated(e.to,e.from)
      PathState(distances,predecessors)
    } else state

  }

  /**
    * Bellman-Ford algorithm.
    *
    * @param g        a weighted digraph.
    * @param source   the source vertex.
    * @tparam A       the vertex type.
    * @return         the path state.
    */
  def bellmanFord[A](g: WeightedDigraph[A], source: A): PathState[A] =
    g.vertices.indices.foldLeft(PathState(source))((xs, _) => g.edges.foldLeft(xs)(relaxEdge))


  /**
    * Check to see if a negative cycle exists within a digraph.
    *
    * @param g        the digraph.
    * @param source   the source vertex.
    * @tparam A       the vertex type.
    * @return         a negative cycle, if one exists otherwise None.
    */
  def negativeCycle[A](g: WeightedDigraph[A], source: A): Option[Seq[A]] = {
    /**
      * WIP!! This will need to be changed !!, we can relax another iteration
      * etc.
      */

    val state = bellmanFord(g,source)

    val maybeCycle = g.edges.flatMap(e=> {
      val (u,v,w) = (e.from,e.to,e.weight)
      if (state.distances(u) + w < state.distances(v)) Some(v)
      else None
    })

    /* Return a negative cycle, if one exists. */
    maybeCycle.headOption.flatMap(v => {
      @tailrec
      def loop(v: A, cycle: Seq[A]) : Seq[A] = {
        val p = state.predecessors(v)
        if (cycle.contains(p)) cycle
        else {
          loop(p, cycle :+ p)
        }
      }
      Some(loop(v,Seq(v)))
    })
  }

}
