package tiki

import tiki.Predef._
import tiki.Poly._
import tiki.Predef.{Boolean, Set}


/**
  * A weighted digraph. This class uses an underlying representation to provide
  * the `Directed` interface.
  *
  * @param edges the stream of weighted edges.
  * @param f  a function that takes a stream of weighted edges and returns `Directed[A]`
  * @tparam A the vertex type.
  */
case class WDigraph[A](edges: Stream[WEdge[A]],
                       f: Stream[WEdge[A]] => Directed[A])
  extends Graph[A,WEdge[A]] with Directed[A] with Weighted[A] {

  val rep = f(edges)
  val weights = edges.foldLeft(Map.empty[(A,A),Double])(
    (acc,v)=> acc.updated((v.edge.from,v.edge.to),v.weight))

  /*
   * Delegate the `Directed` interface to the underlying.
   */
  override def vertices: Stream[A] = rep.vertices
  override def successors(v: A): Set[A] = rep.successors(v)
  override def predecessors(v: A): Set[A] = rep.predecessors(v)
  override def contains(v: A): Boolean = rep.contains(v)

  override def weight(v: A, w: A): Option[Double] = weights.get((v,w))

}

object WDigraph {
  /**
    * Default implementation of `Directed` is the adjacency list.
    * This function takes a stream of edges and returns the adjacency list.
    *
    * @param edges the list of edges.
    * @tparam A the vertex type.
    * @return function `Stream[WEdge..]` => `Directed[A]`
    */
  private def adjacencyList[A](edges: Stream[WEdge[A]]) =
    buildAdjacencyList(edges)

  /**
    * Creates a new weighted digraph from a stream of edges, by
    * default, the underlying implementation is an adjacency list.
    *
    * @param edges  the stream of edges.
    * @tparam A the vertex type.
    * @return a weighted digraph.
    */
  def apply[A](edges: Stream[WEdge[A]]): WDigraph[A] =
    new WDigraph[A](edges, adjacencyList)

}