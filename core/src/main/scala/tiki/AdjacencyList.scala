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
final class AdjacencyList[A](g: Map[A,Set[A]], gr: Map[A,Set[A]]) extends DirectedGraphRep[A] {

  def contains(v: A): Boolean = g.keys.exists(_==v) || gr.keys.exists(_==v)

  def successors(v: A) : Set[A] = g.getOrElse(v,Set.empty[A])

  def predecessors(v: A)  : Set[A] = gr.getOrElse(v,Set.empty[A])

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

}