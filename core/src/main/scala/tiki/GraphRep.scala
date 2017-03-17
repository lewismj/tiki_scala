package tiki

import tiki.Predef._

/**
  * Trait that defines interface for graph representations.
  */
trait GraphRep[A] {
  /**
    * Given a vertex, find its children, i.e. the vertices it has edges to.
    * Returns an option.
    * Note:
    *   A return of None implies that the vertex was not found.
    *   Otherwise Some(set of edges) will be returned. The set may be empty.
    *
    * @param v the vertex.
    * @return a set of vertices, or none if the vertex could not be found.
    */
  def children(v: A) : Option[Set[A]]

  /**
    * Given a vertex, find its parents, i.e. the vertices that have
    * edges to the vertex.
    *
    * Note:
    *   A return of None implies that the vertex was not found.
    *   Otherwise Some(set of edges) will be returned. The set may be empty.
    *
    * @param v the vertex.
    * @return a set of vertices, or none if the vertex could not be found.
    */
  def parents(v: A)  : Option[Set[A]]
}
