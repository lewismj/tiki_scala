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
import scala.math.abs


/**
  * Implicit definitions, e.g. allow use of '-->' operator to represent a
  * directed edge.
  */
object implicits {

  /**
    * Defines implicit operators for constructing edges.
    *
    * @param v      the start vertex.
    * @tparam A     the vertex type.
    */
  final class EdgeDef[A](v: A) {
    def -->(w: A): Edge[A] = new Edge[A](v,w)
  }

  /**
    * Defines implicit operators for constructing labelled edges.
    *
    * @param e    the edge.
    * @tparam A   the type of the vertex.
    * @tparam B   the type of the label.
    */
  final class LabelDef[A,B](e: Edge[A]) {
    def :+(l :B): LabelledEdge[A,B] = new LabelledEdge[A,B](e,l)
  }

  /**
    * Defines implicit operators for constructing weighted edges.
    *
    * @param e    the edge.
    * @tparam A   the type of the vertex.
    */
  final class WeightDef[A](e: Edge[A]) {
    def :#(w: Double): WeightedEdge[A] = new WeightedEdge[A](e,w)
  }

  implicit def anyToEdge[A](v: A): EdgeDef[A] = new EdgeDef[A](v)
  implicit def anyToLEdge[A,B](e: Edge[A]): LabelDef[A,B] = new LabelDef[A,B](e)
  implicit def anyToWEdge[A](e: Edge[A]): WeightDef[A] = new WeightDef[A](e)

  implicit def weightedToEdge[A](s: Stream[WeightedEdge[A]]): Stream[Edge[A]] = s.map(_.edge)
  implicit def labelledToEdge[A,B](s: Stream[LabelledEdge[A,B]]): Stream[Edge[A]] = s.map(_.edge)


  /** Implicit conversions to WeightedGraph. */
  implicit def undirected[A](g: WeightedUndirectedGraph[A]): WeightedGraph[A] = new WeightedGraph[A] {
    def vertices: Stream[A] = g.vertices
    def edges: Stream[WeightedEdge[A]] = g.edges
  }

  /** Implicit conversions to WeightedGraph. */
  implicit def directed[A](g: WeightedDigraph[A]): WeightedGraph[A] = new WeightedGraph[A] {
    def edges: Stream[WeightedEdge[A]] = g.edges
    def vertices: Stream[A] = g.vertices
  }


  /**
    * Transpose a 'Transpose' type T.
    *
    * @param t    the type to transpose.
    * @tparam T   the type of the graph (or other supporting data structure) to transpose.
    * @return     the transposed value.
    */
  def transpose[T: Transpose](t: T): T= Transpose[T].transpose

  /**
    * Implicit implementation of `Transposable` interface for `WeightedDigraph`.
    *
    * @param g    the weighted digraph.
    * @tparam T   the vertex type.
    */
  implicit class Weighted[T](g: WeightedDigraph[T]) extends Transpose[WeightedDigraph[T]] {
    override def transpose: WeightedDigraph[T] = new WeightedDigraph[T] {
      override def edges: Stream[WeightedEdge[T]] = g.edges.map(edge=>tiki.reverse(edge))
      override def predecessors(v: T): Set[T] = g.successors(v)
      override def successors(v: T): Set[T] = g.predecessors(v)
      override def contains(v: T): Boolean = g.contains(v)
      override def vertices: Stream[T] = g.vertices
    }
  }

  /**
    * Implicit implementation of `Transpoable` interface for `Digraph` type.
    *
    * @param g    the digraph.
    * @tparam T   the vertex type.
    */
  implicit class Unweighted[T](g: Digraph[T]) extends Transpose[Digraph[T]] {
    override def transpose: Digraph[T] = new Digraph[T] {
      override def edges: Stream[Edge[T]] = g.edges.map(edge=>edge.to --> edge.from)
      override def predecessors(v: T): Set[T] = g.successors(v)
      override def successors(v: T): Set[T] = g.predecessors(v)
      override def contains(v: T): Boolean = g.contains(v)
      override def vertices: Stream[T] = g.vertices
    }
  }

  /** |-- Implicit required for graph clustering algorithms. */

  /**
    * Provides a 'Real' class, used to implement the ≅ method.
    *
    * @param x  the underlying double.
    */
  final class Real(x: Double) extends Proxy with Ordered[Double] {
    def self: Any = x
    def compare(y: Double): Int = java.lang.Double.compare(x, y)
    def ≅(that: Double)(implicit eps: Double = ε): Boolean = {
      if (x == ∞ && that == ∞ || x == ⧞ && that == ⧞) true
      else abs(x - that) <= eps
    }
  }

  /** Implicit conversion from double to placeholder 'Real'. */
  implicit def double2Real(x: Double): Real = new Real(x)

}
