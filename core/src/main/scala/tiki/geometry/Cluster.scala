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
package geometry

import breeze.linalg.{Axis, DenseMatrix, normalize}
import breeze.numerics.pow
import geometry.Distance._
import geometry.Point._

import scala.annotation.tailrec

object Cluster {

  /**
    * Simplest algorithm for clustering a graph. If the desired number
    * of clusters is known, we can remove k-1 edges with highest
    * weight from the Euclidean minimum spanning tree.
    *
    * @param points the incoming set of points.
    * @param k  the number of clusters.
    * @return a list of lists, each sub-list are the edges between nodes in a cluster.
    */
  def kTrees(points: Vector[Point], k: Int): Vector[WeightedEdge[Point]] =
    euclideanMST(points).sortBy(-_.weight).drop(k - 1).toVector


  /**
    * Specialization of the Markov chain algorithm for points in 2D-space.
    *
    * Note, for points we form a matrix where all distances are calculated
    * (some distances will exist and be non zero, so don't use EMST).
    * For generic graphs, we just record 0 distance if there is no edge.
    *
    * Markov chain algorithm (without optimizations), given a set of points,
    * returns a list of clusters (each cluster is a list of points in the cluster).
    *
    * @param points     the incoming points.
    * @param inflation  the inflation factor.
    * @param expansion  the expansion factor.
    * @param k          the maximum number of iterations.
    * @return list of list of points, sub list represents a cluster.
    */
  def markov(points: Vector[Point], inflation: Double, expansion: Int, k: Int): List[List[Point]] = {
    /* for small n. */
    def mpow(m: DenseMatrix[Double],n: Int): DenseMatrix[Double] = (1 to n).foldLeft(m)((a,_)=> a*m)

    @tailrec
    def mcl(m: DenseMatrix[Double], i: Int): DenseMatrix[Double] = {
      if (i == k) m
      else {
        val m0 = mpow(normalize(pow(m,inflation), Axis._0, 1.0), expansion)
        if (m == m0) m0 /* requires better test. */
        else mcl(m0,i+1)
      }
    }

    /* could add self-loops etc. */
    val m = DenseMatrix.zeros[Double](points.length,points.length)
    m.foreachKey {case (i,j) => if (i != j) m(i,j) = distance(points(i),points(j))}
    val c = mcl(normalize(m, Axis._0, 1.0), 0)

    /* group into clusters and return. */
    val clusters = points.indices.foldLeft(List.empty[List[Point]])((xs,x)=> {
      val cluster = points.indices.foldLeft(List.empty[Point])((ys,y)=> {
        if (c(x,y) > ε) points(y) :: ys else ys
      })
      if (cluster.nonEmpty) cluster :: xs else xs
    })

    clusters
  }

}
