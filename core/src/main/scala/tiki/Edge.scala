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
  * Represents an edge between two vertices.
  *
  * The edges in a graph tend to be either all directed
  * or all undirected. i.e. A property of the graph not
  * really a property of the edge.
  *
  * @param from   one vertex in an edge.
  * @param to     the 'other' vertex in the edge.
  * @tparam A     the type of the vertex.
  */
case class Edge[A](from: A, to: A) {
  /**
    * Returns string representation of the edge.
    *
    * @return a string representation of the edge.
    */
  override def toString: String = s"$from --> $to"
}

/**
  * A labelled edge between two vertices.
  *
  * @param edge   the edge between the two vertices.
  * @param label  the label of the edge.
  * @tparam A     the type of the edge vertex.
  * @tparam B     the type of the label.
  */
case class LEdge[A,B](edge: Edge[A], label: B)  {
  /**
    * Returns one vertex in an edge.
    *
    * @return a vertex of type `A`.
    */
  def from : A = edge.from

  /**
    * Returns the 'other' vertex in the edge.
    *
    * @return a vertex of type `A`.
    */
  def to: A = edge.to

  /**
    * Map the label from one type to another.
    *
    * @param f    the label function, B => C
    * @tparam C   the type of the new label.
    * @return a new labelled edge.
    */
  def lmap[C](f: B => C) : LEdge[A,C] = LEdge(edge,f(label))

  /**
    * Returns a string representation of the labelled edge.
    *
    * @return a string representation of the labelled edge.
    */
  override def toString: String = s"$from --> $to :+ $label"
}
