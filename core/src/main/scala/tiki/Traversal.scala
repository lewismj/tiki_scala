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

import tiki.Predef._

/**
  * Implementation of the basic traversal functions, i.e. depth and breadth first searches.
  */
object Traversal {

  /**
    * Function type that should combine sequences of vertices to traverse
    * into single sequence (see implementation of `dfs` and `bfs`).
    *
    * @tparam A the vertex type.
    */
  private type S[A] = (Seq[A], Seq[A]) => Seq[A]

  /**
    * Traverses a graph representation, given a start vertex. The order of traversal
    * is specified by a function (Seq,Seq) => Seq, which is just used to determine
    * if search should be depth or breadth first.
    *
    * Optionally a stop vertex may be specified. If the vertex is found, the
    * traversal will stop at that point.
    *
    * @param g      the graph representation.
    * @param start  the start vertex.
    * @param f      the traversal sequence.
    * @tparam A     the vertex type.
    * @return       a traversal sequence.
    */
  private def traverse[A](g: GraphRep[A], start: A, stop: Option[A])(f: S[A]): Seq[A] = {
    /*
      * Note: visited & acc contain same elements.
      *  acc is sued to preserve ordering, visited is used because
      *   (Set--Set) will be faster than (Set--Seq).
      *   With space trade-off.
      *
      *   Further investigation into different traversal functions required.
      */
    @tailrec
    def traverse0(remaining: Seq[A], visited: Set[A], acc: Seq[A]): Seq[A] = remaining match {
      case xs if xs.isEmpty => acc
      case _ if Some(remaining.head) == stop => acc :+ remaining.head
      case _ =>
        val xs = f(remaining.tail,(g.adjacent(remaining.head) -- visited).toSeq)
        traverse0(xs, visited + remaining.head,acc :+ remaining.head)
    }
    traverse0(Seq(start),Set.empty,Seq.empty)
  }

  /**
    * Depth first search, given (remainder,next) the order of traversal is next
    * then remainder.
    *
    * @param g        the graph representation.
    * @param start    the start vertex.
    * @param stop     vertex to stop traversal at (optional).
    * @tparam A       the vertex type.
    * @return         a traversal sequence.
    */
  def dfs[A](g: GraphRep[A], start: A, stop: Option[A] = None): Seq[A]
    = traverse(g,start,stop)((r,n) => n ++ r)

  /**
    * Breadth first search, given (remainder,next) the order of traversal is remainder
    * then next.
    *
    * @param g        the graph representation.
    * @param start    the start vertex.
    * @param stop     vertex to stop traversal at (optional).
    * @tparam A       the vertex type.
    * @return         a traversal sequence.
    */
  def bfs[A](g: GraphRep[A], start: A, stop: Option[A] = None): Seq[A]
    = traverse(g,start,stop)((r,n) => r match {
      /* Avoid visiting final node twice, where last remaining node could also be 'next' */
      case _ if r.nonEmpty && r.last == n.head => r ++ n.tail
      case _ => r ++ n
    })

}
