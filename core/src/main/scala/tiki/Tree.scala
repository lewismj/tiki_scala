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

/** Simple RoseTree. */
object Tree {

  /** Forest is a stream of trees. */
  type Forest[A] = Stream[Tree[A]]

  sealed abstract class Tree[A] {
    /** Root node. */
    def rootLabel: A

    /** Sub-forest. */
    def subForest: Forest[A]

    /** leaf flag. */
    def isLeaf: Boolean
  }

  /** Node. */
  case class Node[A](rootLabel: A, subForest: Forest[A]) extends Tree[A] {
    override def isLeaf: Boolean = false
  }

  /** Leaf. */
  case object Leaf extends Tree[Nothing] {
    override def rootLabel: Nothing = throw new NoSuchElementException("Leaf rootLabel is Nothing")
    override def subForest: Forest[Nothing] = throw new NoSuchElementException("Leaf subForest is Nothing.")
    override def isLeaf: Boolean = true
  }

}
