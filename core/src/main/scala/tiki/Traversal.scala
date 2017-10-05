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
package tiki

import cats.free.Trampoline
import cats.Semigroup
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
  def unfold[T,R](z: T)(f: T => Option[(R,T)]): Trampoline[Stream[R]] = f(z) match {
    case None => Trampoline.done(Stream.empty[R])
    case Some((r,v)) => Trampoline.suspend(unfold(v)(f)).flatMap(s => Trampoline.done(r #:: s))
  }

  /**
   * Generates a graph traversal, as stream of vertices.
   *
   * @param g    underlying data structure that supports `Directed`.
   * @param l    the starting list.
   * @tparam A   the type of the vertex.
   * @return     the traversal stream.
   */
  private def traverse[A](g: Directed[A], l: Stream[A])(implicit ev: Semigroup[Stream[A]]): Stream[A]
  = unfold( (l,Stream.empty[A]) ) {
    case (current,visited) => current match {
      case w #:: vs => Some((w, (ev.combine(g.successors(w),vs).diff(visited), visited #::: Stream(w))))
      case _ => None
    }
  }.run


  /**
    * Generates a visit order as a stream of vertices.
    *
    * @param g        underlying data structure that supports `Directed`.
    * @param start    the start vertex.
    * @tparam A       the vertex type.
    * @return         visit order stream.
    */
  private def visit[A](g: Directed[A], start: A)(implicit ev: Semigroup[Stream[A]]): Stream[A]
    = if (g.contains(start)) traverse(g,Stream(start))(ev).distinct else Stream.empty[A]

  /**
    * Perform a depth first search on a directed graph.
    *
    * @param g    underlying data structure that supports `Directed`.
    * @param start    the start vertex.
    * @tparam A       the vertex type.
    * @return         visit order stream.
    */
  def dfs[A](g: Directed[A], start: A): Stream[A] = visit(g,start)((x: Stream[A], y: Stream[A]) => x #::: y)

  /**
    * Perform a breadth first search on a directed graph.
    *
    * @param g        underlying data structure that supports `Directed`.
    * @param start    the start vertex.
    * @tparam A       the vertex type.
    * @return         visit order stream.
    */
  def bfs[A](g: Directed[A], start: A): Stream[A] = visit(g,start)((x: Stream[A], y: Stream[A]) => y #::: x)

}
