package tiki


import tiki.Predef._
import cats.free._
import cats.implicits._

/**
  * Simple stream based unfold traversal.
  */
object Traversal {

  /**
    * Unfold a collection into a stream.
    *
    * @param z    the initial element (type T) of the stream.
    * @param f    f: T => Option ( R, T ), R is the output type.
    * @tparam T   the type of the initial element, i.e. traversal start point.
    * @tparam R   the type of the emitted traversal.
    * @return a stream of the traversal.
    */
  private def unfold[T,R](z:T)(f: T => Option[(R,T)]): Trampoline[Stream[R]] = f(z) match {
    case None => Trampoline.done(Stream.empty[R])
    case Some((r,v)) => Trampoline.suspend(unfold(v)(f)).flatMap(s => Trampoline.done(r #:: s))
  }


  /**
   * Generates a graph traversal, as stream of vertices.
   *
   * @param g    the directed graph representation.
   * @param v    the start vertex.
   * @param dfs  flag to indicate if depth first search (true) or
   *             breadth first search (false).
   * @tparam A   the type of the vertex.
   * @return     the traversal stream.
   */
  private def traverse[A](g: Digraph[A], v: A, dfs: Boolean): Stream[A]
    = unfold( (List(v),Set.empty[A]) ) {
          case (current,visited) => current match {
            case w :: Nil =>
              Some((w, (g.successors(w).toList.filterNot(visited.contains), visited + w)))
            case w :: vs =>
              val next = if (dfs) g.successors(w).toList ::: vs
              else vs ::: g.successors(w).toList
              Some((w, (next.filterNot(visited.contains), visited + w)))
            case _ =>
              None
          }
        }.run

  /**
    * Generates a visit order as a stream of vertices.
    *
    * @param g        the directed graph representation.
    * @param start    the start vertex.
    * @param dfs      true if depth-first search, false for breadth-first search.
    * @tparam A       the vertex type.
    * @return         visit order stream.
    */
  private def visitOrder[A](g: Digraph[A], start: A, dfs: Boolean): Stream[A]
    = if (g.contains(start)) traverse(g, start, dfs).distinct else Stream.empty

  /**
    * Perform a depth first search on a directed graph.
    *
    * @param g        the directed graph representation.
    * @param start    the start vertex.
    * @tparam A       the vertex type.
    * @return         visit order stream.
    */
  def dfs[A](g: Digraph[A], start: A): Stream[A]
    = visitOrder(g,start,dfs=true)

  /**
    * Perform a breadth first search on a directed graph.
    *
    * @param g        the directed graph representation.
    * @param start    the start vertex.
    * @tparam A       the vertex type.
    * @return         visit order stream.
    */
  def bfs[A](g: Digraph[A], start: A): Stream[A]
    = visitOrder(g,start,dfs=false)

}
