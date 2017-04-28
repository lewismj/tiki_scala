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


/**
  * A disjoint set is a data structure that keeps track of a set of
  * elements that are partitioned into a number of disjoint subsets.
  * Useful for finding the number of components within a graph.
  */
final class DisjointSet[A] private (parents: Map[A,A], ranks: Map[A,Long], nc: Long) {

  /** Returns the number of components. */
  def components: Long = nc

  /**
    * Finds the root representative of the element.
    * Note, this doesn't yet implement 'path compression',
    * this would require find returning a new 'DisjointSet'.
    *
    *
    * @param u        the element to find.
    * @return Some representative or None if not found.
    */
  def find(u: A): Option[A] =
    parents.get(u).flatMap(v=> if (u == v) Some(v) else find(v))


  /**
    * Given two vertices `u` and `v` find their representative parents,
    * or None if either or both can't be found.
    *
    * @param u        the first element.
    * @param v        the second element.
    * @return         Some tuple or None.
    */
  private[this] def parents(u: A, v: A): Option[(A,A)] = for {
    x <- find(u)
    y <- find(v)
  } yield (x,y)

  /**
    * Given two vertices `u` and `v` find their ranks or None
    * if either or both can't be found.
    *
    * @param u    the first element.
    * @param v    the second element.
    * @return     Some tuple or None.
    */
  private[this] def ranks(u: A, v:A): Option[(Long,Long)] = for {
    x <- ranks.get(u)
    y <- ranks.get(v)
  } yield (x,y)


  /**
    * Join two partitions together via their elements (i.e. in affect an 'edge').
    *
    * @param u      the first element.
    * @param v      the second element.
    * @return  a new Disjoint set, with the grouped partitions.
    */
  def union(u: A, v:A): Option[DisjointSet[A]] = for {
    (x, y) <- parents(u, v)
    (xr, yr) <- ranks(x, y)
  } yield {
    if (x == y) this
    else (xr,yr) match {
      case _ if xr < yr => new DisjointSet[A](parents.updated(x, y), ranks, nc-1)
      case _ if xr > yr => new DisjointSet[A](parents.updated(y, x), ranks, nc-1)
      case _ => new DisjointSet[A](parents.updated(y, x), ranks.updated(x,xr+1), nc-1)
    }
  }

  /**
    * Return string representation of the disjoint set.
    */
  override def toString: String = {
    s"number of components: $nc\nparents: ${parents.toString}\nranks: ${ranks.toString}\n"
  }

}

object DisjointSet  {

  /**
    * Create the empty Disjoint set for a given type.
    *
    * @tparam T the type of the elements.
    * @return a `DisjointSet[T]`.
    */
  def empty[T] : DisjointSet[T] = new DisjointSet[T](Map.empty,Map.empty,0)

  /**
    * Create a new `DisjointSet`.
    *
    * @param set    the initial set of elements.
    * @tparam T     the type of elements in the set.
    * @return a new `DisjointSet`.
    */
   def apply[T](set: Set[T]): DisjointSet[T] = {
    val parents = set.zip(set).toMap
    val ranks = set.zip(Stream.continually(1L)).toMap
    new DisjointSet[T](parents, ranks, set.size)
  }

}

