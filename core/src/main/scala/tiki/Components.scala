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
import tiki.Traversal._
import tiki.implicits._

/**
  * Defines methods for finding strongly connected components.
  */
object Components {

  /**
    * Currently only Kosaraju's algorithm requires removal of
    * edges. This could be made more efficient.
    */
  private def remove[A](g: Digraph[A], l: List[A]): Digraph[A] = new Digraph[A] {
    override def contains(v: A): Boolean = !l.contains(v) && g.contains(v)
    override def successors(v: A): Set[A] = g.successors(v).filterNot(l.contains)
    /*
      |-- Functions below are not used by Kosaraju's algorithm.
     */
    // $COVERAGE-OFF$
    override def predecessors(v: A): Set[A] = g.predecessors(v).filterNot(l.contains)
    override def vertices: Stream[A] = g.vertices.filterNot(l.contains)
    override def edges: Stream[EdgeLike[A]] = g.edges.filterNot(e=> l.contains(e.from) || l.contains(e.to))
    // $COVERAGE-ON$
  }

  /**
    * Kosaraju's algorithm for finding strongly connected components.
    *
    * @param g    the digraph.
    * @tparam A   the vertex type.
    * @return     the list of strongly connected components.
    */
  def kosaraju[A](g: Digraph[A]): List[List[A]] = {
    @tailrec
    def loop(gr: Digraph[A], s: List[A], scc: List[List[A]]): List[List[A]] = s match {
      case Nil => scc
      case head :: _ =>
        val component = dfs(gr,head).toList
        loop(remove(gr,component), s.diff(component), component :: scc)
    }
    val stack = g.vertices.foldLeft(List.empty[A])((a,v) => dfs(remove(g,a),v).toList ::: a)
    loop(g.transpose,stack,List.empty[List[A]])
  }

}
