---
layout: docs 
title:  "Triangle"
section: "datastructures"
source: "core/src/main/scala/tiki/cluster/Triangle.scala"
scaladoc: "#tiki.cluster.Triangle"
---
# Triangle

In order to perform algorithms such as k-means clustering using graphs, it is
useful to calculate a [Euclidean minimum spanning tree](https://en.wikipedia.org/wiki/Euclidean_minimum_spanning_tree).
The first step is to compute the [Delauney Triangulation](https://en.wikipedia.org/wiki/Delaunay_triangulation) of the
vertices (2D points).

## Usage

## Example

The triangle class is used to calculate the circumcircle of three vertices that may form 
a triangle, as shown below:

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/triangleBisectors.tex)

The is used to triangulate the set of vertices. The [Bowyer-Watson](https://en.wikipedia.org/wiki/Bowyer–Watson_algorithm) algorithm has been implemented.

## Functions

- `ccContains(p)` returns true if the point is within the circumcircle.
- `sharesVertex(that)` returns true if the two triangles share a vertex.
- `toEdges` returns the vector of edges that represent the triangle.