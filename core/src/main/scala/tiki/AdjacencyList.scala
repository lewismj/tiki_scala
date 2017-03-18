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
import Predef._


/**
  * Class represents a set of directed edges as an adjacency list.
  * As the need for undirected graphs is rare, usually represent
  * the undirected graph by two directed edges.
  */
final class AdjacencyList[A] private (g: Map[A,Set[A]], gr: Map[A,Set[A]]) extends DirectedGraphRep[A] {

  /**
    * Returns true if the vertex is contained in the graph.
    * False otherwise.
    *
    * @param v  the vertex.
    * @return flag to indicate if vertex is in the graph.
    */
  def contains(v: A): Boolean = g.keys.exists(_==v) || gr.keys.exists(_==v)

  /**
    * Given a vertex, find its children, i.e. the vertices it has edges to.
    * Returns an option.
    * Note:
    *   A return of None implies that the vertex was not found.
    *   Otherwise Some(set of edges) will be returned. The set may be empty.
    *
    * @param v the vertex.
    * @return a set of vertices, or none if the vertex could not be found.
    */
  def children(v: A) : Option[Set[A]] = g.get(v)

  /**
    * Given a vertex, find its parents, i.e. the vertices that have
    * edges to the vertex.
    *
    * Note:
    *   A return of None implies that the vertex was not found.
    *   Otherwise Some(set of edges) will be returned. The set may be empty.
    *
    * @param v the vertex.
    * @return a set of vertices, or none if the vertex could not be found.
    */
  def parents(v: A)  : Option[Set[A]] = gr.get(v)
}


/**
  * Companion object for the `AdjacencyList` class.
  */
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
    * @param edges  the list of edges.
    * @tparam A     the vertex type.
    * @return a mapping of vertex to child vertices.
    */
  private def edgesToMap[A](edges: Iterable[EdgeLike[A]]) =
    edges.foldLeft(Map.empty[A, Set[A]])((acc, v) => {
        val curr = acc.getOrElse(v.from, Set.empty[A])
        val xs = acc.updated(v.from, curr + v.to)
        if (xs.contains(v.to)) xs else xs.updated(v.to, Set.empty[A])
      })


  /**
    * Create an Adjacency list from a list of directed edges.
    * Note, the reverse will be created.
    *
    * @param edges  the list of edges.
    * @tparam A     the type of the vertex.
    * @return       a new `AdjacencyList`
    */
    def apply[A](edges: Iterable[EdgeLike[A]]): AdjacencyList[A] =
      new AdjacencyList[A](edgesToMap(edges),edgesToMap(edges.map(reverse)))

}