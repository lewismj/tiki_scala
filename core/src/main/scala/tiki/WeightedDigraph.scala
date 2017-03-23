package tiki

import tiki.Predef._
import tiki.Poly._
import tiki.Predef.{Boolean, Set}


/**
  * A simple weighted digraph, based on adjacency list.
  * (experimental,)
  *
  * @param edges the stream of weighted edges.
  * @tparam A the vertex type.
  */
case class WeightedDigraph[A](edges: Stream[WEdge[A]])
  extends Graph[A,WEdge[A]] with Directed[A] with Weighted[A] {

  // 'buildAdjacencyList' ... Could be passed in as
  // f: Stream[WEdge[A]] => Directed[A]
  // if need flexibility of internal rep.

  val rep = buildAdjacencyList(edges)
  val weights = edges.foldLeft(Map.empty[(A,A),Double])(
    (acc,v)=> acc.updated((v.edge.from,v.edge.to),v.weight))

  def vertices: Stream[A] = rep.vertices
  def successors(v: A): Set[A] = rep.successors(v)
  def predecessors(v: A): Set[A] = rep.predecessors(v)
  def contains(v: A): Boolean = rep.contains(v)
  def weight(v: A, w: A): Option[Double] = weights.get((v,w))
}
