package tiki
package instances


trait DigraphInstances {


  implicit class UnweightedT[T](g: Digraph[T]) extends Transpose[Digraph[T]] {
    override def transpose: Digraph[T] = new Digraph[T] {
      override def edges: Stream[Edge[T]] = g.edges.map(edge => Edge(edge.to,edge.from))
      override def predecessors(v: T): Set[T] = g.successors(v)
      override def successors(v: T): Set[T] = g.predecessors(v)
      override def contains(v: T): Boolean = g.contains(v)
      override def vertices: Stream[T] = g.vertices
    }
  }

}
