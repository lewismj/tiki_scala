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
import shapeless.Poly1


/**
  * Implicit definitions, e.g. allow use of '-->' operator to represent a
  * directed edge.
  */
object implicits {

  /**
    * Defines implicit operators for constructing edges.
    *
    * @param v      the start vertex.
    * @tparam A     the vertex type.
    */
  final class EdgeDef[A](v: A) {
    def -->(w: A): Edge[A] = new Edge[A](v,w)
  }

  /**
    * Defines implicit operators for constructing labelled edges.
    *
    * @param e    the edge.
    * @tparam A   the type of the vertex.
    * @tparam B   the type of the label.
    */
  final class LabelDef[A,B](e: Edge[A]) {
    def :+(l :B): LEdge[A,B] = new LEdge[A,B](e,l)
  }

  /**
    * Defines implicit operators for constructing weighted edges.
    *
    * @param e    the edge.
    * @tparam A   the type of the vertex.
    */
  final class WeightDef[A](e: Edge[A]) {
    def :#(w: Double): WEdge[A] = new WEdge[A](e,w)
  }

  implicit def anyToEdge[A](v: A): EdgeDef[A] = new EdgeDef[A](v)
  implicit def anyToLEdge[A,B](e: Edge[A]): LabelDef[A,B] = new LabelDef[A,B](e)
  implicit def anyToWEdge[A](e: Edge[A]): WeightDef[A] = new WeightDef[A](e)


  /**
    * Implicit conversation of edge lists to adjacency list.
    */
  implicit object buildAdjacencyList extends Poly1 {

    /**
      * Builds an adjacency list by folding over the list of edges once.
      *
      * @param edges  the list of edges.
      * @tparam A     the vertex type.
      * @return a mapping of vertex to child vertices.
      */
    private def edgesToMap[A](edges: Iterable[Edge[A]]) =
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
      * @param edges  the list of edges.
      * @tparam A     the type of the vertex.
      * @return       a new `AdjacencyList`
      */
    def makeAdjacencyList[A](edges: Iterable[Edge[A]]): AdjacencyList[A] =
      new AdjacencyList[A](edgesToMap(edges),edgesToMap(edges.map(reverse(_))))


    implicit def edge[A]: Case.Aux[List[Edge[A]],AdjacencyList[A]]
      = at(x => makeAdjacencyList[A](x))

    implicit def labelledEdge[A,B]: Case.Aux[List[LEdge[A,B]],AdjacencyList[A]]
      = at(x => makeAdjacencyList[A](x.map(ledge => ledge.edge)))

    implicit def weightedEdge[A]: Case.Aux[List[WEdge[A]],AdjacencyList[A]]
      = at(x => makeAdjacencyList[A](x.map(wedge => wedge.edge)))

  }

}
