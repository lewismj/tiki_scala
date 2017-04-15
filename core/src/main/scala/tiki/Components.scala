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


object Components {

  /**
    * Currently only Kosaraju's algorithm requires removal of
    * edges. This could be made more efficient.
    */
  private def remove[A](g: Digraph[A], l: List[A]) = new Digraph[A] {
    val vertices0 = g.vertices.filterNot(l.contains)
    val edges0 = g.edges.filterNot(e=> l.contains(e.from) || l.contains(e.to))
    override def contains(v: A): Boolean = vertices0.contains(v)
    override def successors(v: A): Set[A] = g.successors(v).filterNot(l.contains)
    override def predecessors(v: A): Set[A] = g.predecessors(v).filterNot(l.contains)
    override def vertices: Stream[A] = vertices0
    override def edges: Stream[EdgeLike[A]] = edges0
  }

  /**
    * Kosaraju's algorithm for finding strongly connected components.
    * (Experimental).
    *
    * @param g    the digraph.
    * @tparam A   the vertex type.
    * @return     the list of strongly connected components.
    */
  def kosaraju[A](g: Digraph[A]): List[List[A]] = {
    @tailrec
    def loop(g: Digraph[A],s: List[A],scc: List[List[A]]): List[List[A]] = s match {
      case Nil => scc
      case head :: tail =>
        val component = dfs(g,head).toList
        loop(remove(g,component), s.diff(component), component :: scc)
    }

    /*
     * Build dfs based stack based on unfold over the list of all vertices.
     * n.b. Relies on  distinct method preserving the visit order.
     */
    loop( g.transpose,
          traverse(g, g.vertices.toList, dfs=true).distinct.toList,
          List.empty[List[A]])
  }

}
