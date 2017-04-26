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
package tests

import tiki.Predef._
import tiki.implicits._
import tiki.tests.arbitrary.AllArbitrary


class DisjointSetSpec extends TikiSuite with AllArbitrary {

  test("Initial number of components equals size of the set.")(forAll { (xs: Set[Int]) =>
    DisjointSet(xs).components should equal(xs.size)
  })

  test("Union of all elements should yield a single component.")(forAll { (xs: Set[Int]) => {
    val ys = xs.iterator.sliding(2)
    val disjointSet = ys.foldLeft(DisjointSet(xs))((acc, v) => acc.union(v.head, v.last).getOrElse(acc))
    if (xs.nonEmpty) disjointSet.components should equal(1) else disjointSet.components should equal(0)
  }})

  test("Union of all elements should yield a single component .2") {
    /* force path in union method. */
    val disjointSet = List(1 --> 2, 3 --> 1).foldLeft(DisjointSet(Set(1,2,3)))((acc, e)=> {
      val union = acc.union(e.from, e.to)
      union.getOrElse(acc)
    })
    disjointSet.components should be (1)
  }

  test("Find of an element not in the set should return None.")(forAll { (xs: Set[Int], ys: Set[Int]) => {
    val d = xs.diff(ys)
    val disjointSet = DisjointSet(ys)
    if (d.nonEmpty) d.forall(disjointSet.find(_).isEmpty) should be (true)
  }})

  test("Find of an element in the set should be defined.")(forAll { (xs: Set[Int]) => {
    val disjointSet = DisjointSet(xs)
    xs.forall(disjointSet.find(_).isDefined) should be (true)
  }})

  test("Union of elements in the same partition should return the same disjoint set.") {
    val set = DisjointSet(Set(1,2)).union(1,2)
    set should be (set.getOrElse(DisjointSet.empty[Int]).union(1,2))
  }

  test("Number of components of an empty set is zero.") {
    DisjointSet(Set.empty[Int]).components should equal(0)
  }

  test("Number of components of an DisjointSet.empty is zero.") {
    DisjointSet.empty.components should equal(0)
  }

}
