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
  * Base representation for a graph.
  *
  * @tparam A the vertex type.
  */
trait GraphRep[A] {

  /**
    * Returns true if the vertex is contained in the graph.
    * False otherwise.
    *
    * @param v  the vertex.
    * @return flag to indicate if vertex is in the graph.
    */
  def contains(v: A): Boolean

  /**
    * Return the vertices adjacent to the given vertex.
    * If no adjacent vertices return empty set.
    * None will be returned if the vertex is not contained
    * in the graph.
    *
    * @param v  the vertex.
    * @return the adjacent vertices.
    */
  def adjacent(v: A): Option[Set[A]]

}


/**
  * Trait that defines interface for graph representations.
  */
trait DirectedGraphRep[A] extends GraphRep[A] {

  /**
    * Returns true if the vertex is contained in the graph.
    * False otherwise.
    *
    * @param v  the vertex.
    * @return flag to indicate if vertex is in the graph.
    */
  def contains(v: A): Boolean

  /**
    * Return the vertices adjacent to the given vertex.
    * For a directed graph, this is the children of the vertex.
    *
    * @param v  the vertex.
    * @return the adjacent vertices.
    */
  override def adjacent(v: A): Option[Set[A]] = children(v)

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
  def children(v: A): Option[Set[A]]

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
  def parents(v: A): Option[Set[A]]
}
