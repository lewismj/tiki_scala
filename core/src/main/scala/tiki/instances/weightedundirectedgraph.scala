package tiki
package instances

import tiki.{WeightedEdge, WeightedGraph, WeightedUndirectedGraph}


trait WeightedUndirectedInstances {

  implicit def undirected[A](g: WeightedUndirectedGraph[A]): WeightedGraph[A] = new WeightedGraph[A] {
    def vertices: Stream[A] = g.vertices
    def edges: Stream[WeightedEdge[A]] = g.edges
  }

}
