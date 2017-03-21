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
  * Implicit definitions.
  */
object implicits {


  /**
    * Defines implicit operators for constructing edges.
    *
    * @param v      the start vertex.
    * @tparam A     the vertex type.
    */
  final class EdgeDef[A](v: A) {
    /**
      * Creates a new edge from 'v' to 'w'.
      *
      * @param w  the 'to' vertex.
      * @return a new `Edge` object.
      */
    def -->(w: A): Edge[A] = new Edge[A](v,w)
  }

  /**
    * Defines implicit operators for contructing labelled edges.
    *
    * @param e    the edge.
    * @tparam A   the type of the vertex.
    * @tparam B   the type of the label.
    */
  final class LabelDef[A,B](e: Edge[A]) {
    /**
      * Apply a label to an edge to create a labelled edge.
      *
      * @param l  the label to apply to the edge.
      * @return a new `LEdge` object.
      */
    def :+(l :B): LEdge[A,B] = new LEdge[A,B](e,l)
  }

  /**
    * Implicitly create an `EdgeDef`.
    *
    * @param v    the start vertex.
    * @tparam A   the type of the vertex.
    * @return     a new `EdgeDef` object.
    */
  implicit def anyToEdge[A](v: A): EdgeDef[A] = new EdgeDef[A](v)

  /**
    * Implicitly create a `LabelDef`.
    *
    * @param e    the edge.
    * @tparam A   the type of the vertex.
    * @tparam B   the type of the label.
    * @return     a new `LabelDef` object.
    */
  implicit def anyToLEdge[A,B](e: Edge[A]): LabelDef[A,B] = new LabelDef[A,B](e)


  /**
    * Implicit conversation of edge lists to adjacency list.
    */
  implicit object buildAdjacencyList extends Poly1 {

    implicit def edge[A] : Case.Aux[Iterable[Edge[A]],AdjacencyList[A]]
      = at(x => AdjacencyList[A](x))

    implicit def labelledEdge[A,B] : Case.Aux[Iterable[LEdge[A,B]],AdjacencyList[A]]
      = at(x => AdjacencyList[A](x.map(ledge => ledge.edge)))

    /*
      Provide 'List' implementation as its the most common 'Iterable' used and will avoid
      calling code having to add '.toIterable'.
     */
    implicit def edgeList[A] : Case.Aux[List[Edge[A]],AdjacencyList[A]]
      = at(x => AdjacencyList[A](x))

    implicit def labelledEdgeList[A,B] : Case.Aux[List[LEdge[A,B]],AdjacencyList[A]]
      = at(x => AdjacencyList[A](x.map(ledge => ledge.edge)))

  }

}
