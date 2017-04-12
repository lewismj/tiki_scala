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
import tiki.Types._

object Zipper {

  type Parent[A] = (Forest[A],A,Forest[A])

  /**
    * See: https://hackage.haskell.org/package/rosezipper-0.2/src/Data/Tree/Zipper.hs
    *      By Krasimir Angelov & Iavor S. Diatchki 2008.
    */
  case class Zipper[A](focus: Tree[A], before: Forest[A], after: Forest[A], parents: Stream[Parent[A]]) {

    def fromZipper: Tree[A] = toTree
    def moveLeft: Option[Zipper[A]] = prevTree
    def moveRight: Option[Zipper[A]] = nextTree
    def getLabel: A = focus.rootLabel

    /** -- Moving around --------------------------------------------------------------- */
    private[this] def forest(before: Forest[A], n: Tree[A], after: Forest[A]): Forest[A]
    = before.foldLeft(n #:: after)((y,tree) => tree #:: y )

    /* -- | The parent of the given location. */
    def parent: Option[Zipper[A]] =  parents match {
      case (ls,a,rs) #:: ps => Some(Zipper(Node(a,forest(before,focus,after)),ls,rs,ps))
    }

    /* -- | The top-most parent of the given location. */
    @tailrec
    final def root: Zipper[A] = parent match {
      case Some(p) => p.root
      case None => this
    }

    /* -- | The tree before this location, if any. */
    def prevTree: Option[Zipper[A]] = before match {
      case t #:: ts => Some(Zipper(t,ts, focus #:: after,parents))
      case _ => None
    }

    /* -- | The tree after this location, if any. */
    def nextTree: Option[Zipper[A]] = after match {
      case t #:: ts => Some(Zipper(t, focus #:: before, ts, parents))
      case _ => None
    }

    /* parents after moving down. */
    private[this] def down: Stream[Parent[A]] = (before,focus.rootLabel,after) #:: parents

    /* -- | The first child of the given location. */
    def firstChild: Option[Zipper[A]] = focus.subForest match {
      case t #:: ts => Some(Zipper(t,Stream.empty,ts,down))
      case _ => None
    }

    /* -- | The last child of the given location. */
    def lastChild: Option[Zipper[A]] = focus.subForest match {
      case t #:: ts => Some(Zipper(t,ts,Stream.empty,down))
      case _ => None
    }

    /** -- Conversions ----------------------------------------------------------------- */
    def toTree: Tree[A] = root.focus

    /** -- The current tree ------------------------------------------------------------ */

    /* --| Change the current tree. */
    def setTree(a: Tree[A]): Zipper[A] = Zipper(a,before,after,parents)

    /* --| Modify the current tree. */
    def modifyTree(f: Tree[A] => Tree[A]): Zipper[A] = setTree(f(focus))

    /* --| Change the label at the current node. */
    def setLabel(a: A): Zipper[A] = modifyTree(t => Node(a,t.subForest))
  }

}
