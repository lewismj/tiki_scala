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
import tiki.implicits._

/**
  * Defines a 'directed' interface that some graph
  * representations may support.
  */
trait Directed[A] {
  /**
    * Returns true if the vertex is contained in the graph.
    *
    * @param v  the vertex.
    * @return true, if the vertex is in the graph, false otherwise.
    */
  def contains(v: A): Boolean

  /**
    * Returns all vertices that the given vertex has an out-edge to.
    *
    * @param v  the vertex.
    * @return the set of vertices that the given vertex has an out-edge to.
    */
  def successors(v: A): Set[A]

  /**
    * Returns all vertices that have an out-edge to the given vertex.
    *
    * @param v the vertex.
    * @return the set of vertices that have an out-edge to the given vertex.
    */
  def predecessors(v: A): Set[A]
}

/**
  * Defines the `Weighted` interface.
  *
  * @tparam A the vertex type.
  */
trait Weighted[A] {
  /**
    * The weight of the edge from u to v.
    */
  def weight(u: A, v: A): Option[Double]
}

/**
  * Base representation for a graph.
  *
  * @tparam A the vertex type.
  */
trait Graph[A] {
  /**
    * Return a stream of vertices.
    *
    * @return the stream of vertices.
    */
  def vertices: Stream[A]

  /**
    * Returns a stream of edges.
    *
    * @return the stream of edges.
    */
  def edges: Stream[EdgeLike[A]]
}

/**
  * A directed graph (a graph that supports the `Directed` interface.
  *
  * @tparam A the vertex type.
  */
trait Digraph[A] extends Graph[A] with Directed[A] {}

/**
  * Weighted digraph, a digraph that returns weighted edges.
  *
  * @tparam A the vertex type.
  */
trait WeightedDigraph[A] extends Digraph[A] with Weighted[A] {
  override def edges: Stream[WeightedEdge[A]]

  /**
    * Provide default implementation.
    * Representations can override for efficiency.
    */
  override def weight(u: A, v: A): Option[Double] =
    edges.find(_.edge == u --> v).flatMap(we => Some(we.weight))
}
