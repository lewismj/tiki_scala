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


/** Very simple implementation of adjacency list. */
final class AdjacencyList[A](val g: Map[A,Set[A]], val gr: Map[A,Set[A]]) extends Directed[A] {
  lazy val vertices: Stream[A] = g.keys.toSet.union(gr.keys.toSet).toStream
  def contains(v: A): Boolean = vertices.contains(v)
  def successors(v: A): Stream[A] = g.getOrElse(v,Set.empty[A]).toStream
  def predecessors(v: A): Stream[A] = gr.getOrElse(v,Set.empty[A]).toStream
}

object AdjacencyList {

  /**
    * Create the empty Adjacency list set for a given type.
    *
    * @tparam A the type of the elements.
    * @return a `DisjointSet[T]`.
    */
  def empty[A] : AdjacencyList[A] =
    new AdjacencyList[A](Map.empty[A,Set[A]], Map.empty[A,Set[A]])

  /**
    * Builds an adjacency list by folding over the list of edges once.
    *
    * @param edges the list of edges.
    * @tparam A the vertex type.
    * @return a mapping of vertex to child vertices.
    */
  private def edgesToMap[A](edges: Stream[Edge[A]]) =
  edges.foldLeft(Map.empty[A, Set[A]])((acc, v) => {
    val curr = acc.getOrElse(v.from, Set.empty[A])
    val xs = acc.updated(v.from, curr + v.to)
    if (xs.contains(v.to)) xs else xs.updated(v.to, Set.empty[A])
  })

  /**
    * Create an adjacency list from a list of directed edges.
    *
    * Note: At present we don't store the edge type information, this can
    * be retained by the graph representation (i.e. we strip the edge to
    * just the to/from vertices).
    *
    * @param edges the list of edges.
    * @tparam A the type of the vertex.
    * @return a new `AdjacencyList`
    */
  def apply[A](edges: Stream[Edge[A]]): AdjacencyList[A] =
    new AdjacencyList[A](edgesToMap(edges), edgesToMap(edges.map(e=>Edge(e.to,e.from))))

}