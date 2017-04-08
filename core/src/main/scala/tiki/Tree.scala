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

import cats._
import cats.implicits._


/** Rose Tree
  * See: http://hackage.haskell.org/package/containers-0.5.10.2/docs/Data-Tree.html
  * WIP!
  */
  sealed trait Tree[T] {
    self =>Foldable

  /** Label value. */
  def rootLabel: T

  /** Zero or more child trees. */
  def subForest: Stream[Tree[T]]

  /** -- | The elements of a tree in pre-order. */
  def flatten: Stream[T] = squish(Stream.empty)

  def squish(xs: Stream[T]): Stream[T] =
    rootLabel #:: subForest.foldRight(xs)(_.squish(_))

  /** -- | Lists of nodes at each level of the tree. */
  def levels: Stream[Stream[T]] = {
    val f = (s: Stream[Tree[T]]) => Foldable[Stream].foldMap(s)(_.subForest)
    Stream.iterate(Stream(this))(f) takeWhile (_.nonEmpty) map (_ map (_.rootLabel))
  }

}

object Tree {

  object Node {

    def apply[A](root: => A, forest: => Stream[Tree[A]]): Tree[A] = new Tree[A] {
      lazy val rootLabel = root
      lazy val subForest = forest
    }

    def unapply[A](t: Tree[A]): Option[(A, Stream[Tree[A]])] = Some((t.rootLabel, t.subForest))
  }

  object Leaf {

    def apply[A](root: => A): Tree[A] = Node(root, Stream.empty)

    def unapply[A](t: Tree[A]): Option[A] = t match {
      case Node(root, Stream.Empty) => Some(root)
      case _ => None
    }

  }

}
