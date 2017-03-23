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
package object tiki {
  import shapeless.{:+:, CNil}
  import tiki.AdjacencyList._
  import tiki.Predef._
  import tiki.implicits._
  import shapeless.Poly1

  /** Edge union */
  type EdgeLike[A,B] = Edge[A] :+: WeightedEdge[A] :+: LabelledEdge[A,B] :+: CNil


  /**
    * Provides 'reverse' function for different 'Edge' case classes.
    */
  object reverse extends Poly1 {
    implicit def edge[A] : Case.Aux[Edge[A],Edge[A]]= at({x=> x.to --> x.from})
    implicit def labelledEdge[A,B] : Case.Aux[LabelledEdge[A,B],LabelledEdge[A,B]]
      = at({ x=> x.edge.to --> x.edge.from :+ x.label})
    implicit def weightedEdge[A] : Case.Aux[WeightedEdge[A],WeightedEdge[A]]
      = at({ x=> x.edge.to --> x.edge.from :# x.weight})
  }

  /**
    * Builds an adjacency list, given any type of edge.
    */
  implicit object buildAdjacencyList extends Poly1 {
    implicit def caseEdge[A]: Case.Aux[List[Edge[A]], AdjacencyList[A]]
      = at(x => AdjacencyList[A](x))
    implicit def caseLEdge[A, B]: Case.Aux[List[LabelledEdge[A, B]], AdjacencyList[A]]
      = at(x => AdjacencyList[A](x.map(ledge => ledge.edge)))
    implicit def caseWEdge[A]: Case.Aux[List[WeightedEdge[A]], AdjacencyList[A]]
      = at(x => AdjacencyList[A](x.map(wedge => wedge.edge)))
  }

}

