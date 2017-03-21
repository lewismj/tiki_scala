package tiki

import tiki.Predef._

/**
  * Interface for weighted edges.
  *
  * @tparam A the vertex type.
  */
trait WEdgeLike[A] extends EdgeLike[A] {
  /** The weight of the edge. */
  def weight: Double
}

/**
  * A weighted edge between two vertices.
  *
  * @param edge   the edge between the two vertices.
  * @param weight the weight of the edge.
  * @tparam A     the type of the edge vertex.
  */
case class WEdge[A](edge: Edge[A], weight: Double)  extends WEdgeLike[A] {
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
    * Returns a string representation of the weighted edge.
    *
    * @return a string representation of the weighted edge.
    */
  override def toString: String = s"$from --> $to # $weight"
}
