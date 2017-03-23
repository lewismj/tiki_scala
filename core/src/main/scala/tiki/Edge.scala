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
  * The edges in a graph tend to be either all directed or all undirected.
  * i.e. A property that holds across the graph. An undirected graph can
  * be represented by a pair of directed edges. Generally, the library is
  * concerned with directed graphs.
  *
  * @param from   one vertex in an edge.
  * @param to     the 'other' vertex in the edge.
  * @tparam A     the type of the vertex.
  */
case class Edge[A](from: A, to: A)  {
  override def toString: String = s"$from --> $to"
}

/**
  * A labelled edge between two vertices.
  */
case class LEdge[A,B](edge: Edge[A], label: B)  {

  def from : A = edge.from
  def to: A = edge.to
  def map[C](f: B => C): LEdge[A,C] = LEdge(edge,f(label))

  override def toString: String = s"$from --> $to :+ $label"
}

/**
  * A weighted edge between two vertices.
  */
case class WEdge[A](edge: Edge[A], weight: Double) {

  def from : A = edge.from
  def to: A = edge.to
  def map(f: Double => Double): WEdge[A] = WEdge(edge,f(weight))

  override def toString: String = s"$from --> $to :# $weight"
}
