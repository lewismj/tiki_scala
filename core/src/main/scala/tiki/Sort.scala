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

object Sort {
  
  /**
    * Topological sort of a digraph. Returns None if a cycle is found.
    *
    * @param g    the digraph.
    * @tparam A   the vertex type.
    * @return     the topological sort.
    */
  def tsort[A](g: Digraph[A]): Option[Stream[A]] = {
    @tailrec
    def kahn(s0: Stream[A], l: Stream[A], ys: Stream[EdgeLike[A]]): Option[Stream[A]] = s0 match {
      case _ if (s0 isEmpty) && (ys isEmpty) => Some(l)
      case _ if s0 isEmpty => None
      case n #:: tail =>
        val (edgesFrom, remainder) = ys.partition(_.from == n)
        val maybeInsert = edgesFrom.map(_.to)
        val insert = maybeInsert.filterNot(remainder.map(_.to).contains(_))
        kahn(tail #::: insert, l :+ n, remainder)
    }
    kahn(g.vertices.filterNot(g.edges.map(_.to).contains(_)), Stream.empty, g.edges)
  }

  /**
    * Returns true if the graph has a cycle, false otherwise.
    *
    * @param g    the digraph.
    * @tparam A   the vertex type.
    * @return     true, if graph has cycle.
    */
  def hasCycle[A](g: Digraph[A]): Boolean = tsort(g).isEmpty

}
