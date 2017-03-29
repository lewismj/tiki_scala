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
package tiki.tests.arbitrary

import org.scalacheck.Arbitrary._
import org.scalacheck.{Arbitrary, Gen}
import tiki.Predef._
import tiki.WeightedEdge
import tiki.implicits._


trait ArbitraryWeightedEdgeList extends ArbitrarySet {

  def weightedEdgeList[A: Arbitrary] : Gen[List[WeightedEdge[A]]] = for {
    xs <- arbitrary[Set[A]]
    ys <- arbitrary[Double]
    if xs.nonEmpty
  } yield {
    xs.iterator.sliding(2).foldLeft(List.empty[WeightedEdge[A]])(
      (acc, v) => (v.head --> v.last :# ys) :: acc)
  }

  def weightedEdgeStream[A: Arbitrary]: Gen[Stream[WeightedEdge[A]]]
  = for (xs <- arbitrary[List[WeightedEdge[A]]]) yield xs.toStream

  implicit def arbitraryWeightedEdgeStream[A:Arbitrary]: Arbitrary[Stream[WeightedEdge[A]]]
  = Arbitrary(weightedEdgeStream)

  implicit def arbitraryWeightedEdgeList[A:Arbitrary]: Arbitrary[List[WeightedEdge[A]]]
    = Arbitrary(weightedEdgeList)
}
