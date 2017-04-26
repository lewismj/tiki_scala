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
package tree

import cats.Foldable
import cats.implicits._
import tiki.Predef._


object RoseTree {

  /** Forest is a stream of trees. */
  type Forest[A] = Stream[Tree[A]]

  /**
    * Simplified tree.
    * See: http://hackage.haskell.org/package/containers-0.5.10.2/docs/src/Data-Tree.html
    */
  sealed abstract class Tree[A] {
    self => Foldable

    /** Root node. */
    def rootLabel: A

    /** Sub-forest. */
    def subForest: Forest[A]

    /** -- | Lists of nodes at each level of the tree. */
    def levels: Stream[Stream[A]] = {
      val f = (s: Stream[Tree[A]]) => Foldable[Stream].foldMap(s)(_.subForest)
      Stream.iterate(Stream(this))(f).takeWhile(_.nonEmpty).map(_.map(_.rootLabel))
    }

    /** wip: fold entire stream. */
    private def squish(xs: Stream[A]): Stream[A] =
      rootLabel #:: subForest.foldRight(xs)(_.squish(_))

    /** -- | The elements of a tree in pre-order. */
    def flatten: Stream[A] = squish(Stream.empty)

    /** Apply f to the nodes of the tree. */
    def map[B](f: A => B): Tree[B] =
      Node(f(rootLabel),subForest.map(_.map(f)))
  }

  /** Node. */
  case class Node[A](rootLabel: A, subForest: Forest[A]) extends Tree[A]
}

