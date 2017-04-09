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
package data

import tiki.Predef._
import tiki.data.Tree.Node
import tiki.data.TreeZipper.{Context, Forest, Parent}



object TreeZipper {

  type Forest[A] = Stream[Tree[A]]

  case class Parent[A](lf: Forest[A], v: A, rf: Forest[A]) {
    def map[B](f: A => B): Parent[B]
      = Parent(lf.map(tree => tree.map(f)), f(v), rf.map(tree => tree.map(f)))
  }

  case class Context[A](node: Tree[A], lf: Forest[A], rf: Forest[A], parents: Stream[Parent[A]]) {
    def map[B](f: A => B): Context[B]
      = Context(node.map(f), lf.map(_.map(f)), rf.map(_.map(f)),parents.map(_.map(f)))
  }

}

case class TreeZipper[A](context: Context[A]) {

  def map[B](f: A => B): TreeZipper[B] = TreeZipper(context.map(f))

  def updateTree(f: Tree[A] => Tree[A]): TreeZipper[A]
    = TreeZipper(context.copy(node =f(context.node)))

  def parent: Option[Context[A]] = context.parents match {
    case Parent(lf,v,rf) #:: tail =>
      val n = Node(v,mergeForest(context.node,context.lf,context.rf))
      Some(Context(n,lf,rf,tail))
    case _ => None
  }

  def mergeForest(n: Tree[A], fa: Forest[A], fb: Forest[A]): Forest[A]
    = fa.foldLeft(n #:: fb)((y,tree)=> tree #:: y )

}