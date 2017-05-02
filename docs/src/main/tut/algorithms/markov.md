---
layout: docs 
title:  "Markov Cluster Algorithm"
section: "algorithms"
source: "core/src/main/scala/tiki/Markov.scala"
scaladoc: "#tiki.Markov"
---
# Markov Cluster Algorithm

Stijn van Dongen's MCL algorithm, cluster algorithm for graphs based on simulation of (stochastic) flow.

## Example

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/markov.png)

```tut
import tiki._
import tiki.implicits._
import tiki.Markov._

val graph = new Graph[Int] {
  override def vertices: Stream[Int] = Stream(0, 1, 2, 3, 4, 5)
  override def edges: Stream[Edge[Int]] =
    Stream(0 --> 1, 1 --> 2, 0 --> 2, 2 --> 3, 3 --> 4, 3 --> 5, 4 --> 5)
}
val clusters = clustersOf(graph, 2, 2, 10)
```

## Functions

- _`clustersOf(g,inflation,expansion,k)`_


## Implementation

The central part of the algorithm is very simple, as shown below:

```scala
  @tailrec
  private def mcl(m: DenseMatrix[Double], inflation: Double, expansion: Int, k: Int): DenseMatrix[Double] = {
    if (k == 0) m
    else {
      val m0 = mpow(normalize(pow(m, inflation), Axis._0, 1.0), expansion)
      if (m == m0) m0 /* requires better test. */
      else mcl(m0, inflation, expansion, k-1)
    }
  }
```
