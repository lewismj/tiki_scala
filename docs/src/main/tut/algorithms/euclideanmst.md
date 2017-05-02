---
layout: docs 
title:  "Euclidean Minimum Spanning Tree"
section: "algorithms"
source: "core/src/main/scala/tiki/clustering/Distance.scala"
scaladoc: "#tiki.clustering.Distance"
---
# Euclidean Minimum Spanning Tree

From [wikipedia](https://en.wikipedia.org/wiki/Euclidean_minimum_spanning_tree)
"_The Euclidean minimum spanning tree or EMST is a minimum spanning 
tree of a set of n points in the plane (or more generally in ℝd), 
where the weight of the edge between each pair of points is the 
Euclidean distance between those two points. In simpler terms, an EMST 
connects a set of dots using lines such that the total length of all the 
lines is minimized and any dot can be reached from any other by following the lines._"


The implementation is straightforward, run a triangulation over the set of input points
to generate a set of edges. Form an undirected weighted graph from those edges. The 
weight of each edge is the distance between the points.
Then run Kruskal's algorithm over the graph to generate the Euclidean minimum spanning tree.

## Example

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/emst.png)

## Functions

- _`ecludeanMST(points)`_ returns the list of edges (weighted by distance) that form the Euclidean minimum
spanning tree.

```scala
  def euclideanMST(points: Vector[Point]): List[WeightedEdge[Point]] = {
    points match {
      case xs if xs.length < 2 =>
        List.empty[WeightedEdge[Point]]
      case xs if xs.length == 2 =>
        List(points.head --> points.last :# distance(points.head,points.last))
      case _ =>
        val graph = new WeightedUndirectedGraph[Point] {
          override def vertices: Stream[Point] = points.toStream
          override def weights: Stream[WeightedEdge[Point]]
            = bowyerWatson(points).map(e=> e.from --> e.to :# distance(e.from,e.to)).toStream
        }
        kruskal(graph)
    }
  }
```

## K-Spanning trees

It is possible to trivially cluster points into _k_ clusters by removing the _k-1_ edges that have the heighest
weight.

```scala
  def kTrees(points: Vector[Point], k: Int): Vector[WeightedEdge[Point]] =
    euclideanMST(points).sortBy(-_.weight).drop(k - 1).toVector
```

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/kspanning.png)
