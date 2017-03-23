package tiki

import tiki.Predef._
import shapeless.Poly1


object Poly {
  /**
    * Implicit conversation of edge lists to adjacency list.
    */
  implicit object buildAdjacencyList extends Poly1 {

    /**
      * Builds an adjacency list by folding over the list of edges once.
      *
      * @param edges the list of edges.
      * @tparam A the vertex type.
      * @return a mapping of vertex to child vertices.
      */
    private def edgesToMap[A](edges: Iterable[Edge[A]]) =
      edges.foldLeft(Map.empty[A, Set[A]])((acc, v) => {
        val curr = acc.getOrElse(v.from, Set.empty[A])
        val xs = acc.updated(v.from, curr + v.to)
        /* make sure leaf vertices are in the edge map.*/
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
    def makeAdjacencyList[A](edges: Iterable[Edge[A]]): AdjacencyList[A] =
      new AdjacencyList[A](edgesToMap(edges), edgesToMap(edges.map(reverse(_))))

    implicit def caseEdge[A]: Case.Aux[List[Edge[A]], AdjacencyList[A]]
      = at(x => makeAdjacencyList[A](x))

    implicit def caseLEdge[A, B]: Case.Aux[List[LabelledEdge[A, B]], AdjacencyList[A]]
      = at(x => makeAdjacencyList[A](x.map(ledge => ledge.edge)))

    implicit def caseWEdge[A]: Case.Aux[List[WeightedEdge[A]], AdjacencyList[A]]
      = at(x => makeAdjacencyList[A](x.map(wedge => wedge.edge)))
  }


}
