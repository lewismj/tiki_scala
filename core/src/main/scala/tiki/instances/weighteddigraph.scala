package tiki
package instances

trait WeightedDigraphInstances {

  implicit def directed[A](g: WeightedDigraph[A]): WeightedGraph[A] = new WeightedGraph[A] {
    def edges: Stream[WeightedEdge[A]] = g.edges
    def vertices: Stream[A] = g.vertices
  }

}
