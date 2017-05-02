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

import breeze.linalg.{Axis, DenseMatrix, normalize}
import breeze.numerics._
import scala.annotation.tailrec


object Markov {

  /**
    * Markov clustering algorithm.
    */
  @tailrec
  private def mcl(m: DenseMatrix[Double], inflation: Double, expansion: Int, k: Int): DenseMatrix[Double] = {
    if (k == 0) m
    else {
      val m0 = mpow(normalize(pow(m, inflation), Axis._0, 1.0), expansion)
      if (m == m0) m0 /* requires better test. */
      else mcl(m0, inflation, expansion, k-1)
    }
  }

  /**
    * Returns the clusters of the unweighted graph, using Markov cluster algorithm.
    *
    * For weighted graphs:
    *   We would form a dense-graph (e.g. see Point specialization in
    *   the'geometry' package) where  i-j is the distance between vertices i,j
    */
  def clustersOf[A](g: Graph[A], inflation: Double, expansion: Int, k: Int): List[Set[A]] = {
    require(k>=1,"Max. number of iterations must be >= 1.")
    val m = DenseMatrix.zeros[Double](g.vertices.length,g.vertices.length)

    /* todo: use AdjacencyMatrix representation to avoid building inside algorithm. */
    val points = g.vertices.zip(Stream.from(0)).toMap
    val indices = Stream.from(0).zip(g.vertices).toMap
    g.edges.foreach(e=> {
      val i = points(e.from)
      val j = points(e.to)
      m(i,j) = 1.0
      m(j,i) = 1.0
    })
    g.vertices.indices.foreach(i => m(i,i) = 1.0)

    /* calculate Markov clustering & return clusters. */
    val c = mcl(normalize(m, Axis._0, 1.0), inflation, expansion, k)
    val clusters = g.vertices.indices.foldLeft(List.empty[Set[A]])((xs,x) => {
      val cluster = g.vertices.indices.foldLeft(Set.empty[A])((ys,y)=> {
        if (c(x,y) > μ) ys + indices(y)  else ys
      })
      if (cluster.nonEmpty) cluster :: xs else xs
    })
    clusters
  }

}
