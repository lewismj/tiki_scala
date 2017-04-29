---
layout: docs 
title:  "Triangulation"
section: "algorithms"
source: "core/src/main/scala/tiki/clustering/Triangulation.scala"
scaladoc: "#tiki.clustering.Triangulation"
---

## Triangulation

In order to perform graph clustering, we need to calculate a Euclidean minimum
distance spanning tree. The first step is to perform a Delaunay triangulation.
One of the simplest methods is the Bowyer-Watson algorithm.


From [wikipedia](https://en.wikipedia.org/wiki/Bowyer–Watson_algorithm)

"_After every insertion, any triangles whose circumcircles contain 
the new point are deleted, leaving a star-shaped polygonal 
hole which is then re-triangulated using the new point. 
By using the connectivity of the triangulation to efficiently 
locate triangles to remove, the algorithm can take O(N log N) 
operations to triangulate N points, although special degenerate 
cases exist where this goes up to O(N2)._"

## Example

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/triangulation.png)

## Bowyer-Watson

This is implemented as follows:

```scala
  def bowyerWatson(points: Vector[Point]): List[Edge[Point]] = {

    case class BadTriangles(triangles: Vector[Triangle], edges: Vector[Edge[Point]])
    val boundary = superTriangle(minMax(points))

    val triangles = points.foldLeft(List(boundary))((ts, pt)=> {

      val init = BadTriangles(Vector.empty[Triangle],Vector.empty[Edge[Point]])

      val BadTriangles(xs,ys) = ts.foldLeft(init)((bt, t) => {
        if (t.ccContains(pt))
          BadTriangles(t +: bt.triangles, Vector(t.a --> t.b, t.b --> t.c, t.c --> t.a) ++ bt.edges)
        else bt
      })

      val polygon = ys.groupBy(e=> if (e.from.r2 > e.to.r2) e else e.to --> e.from)
                    .filterNot(_._2.size>1).keys

      polygon.foldLeft(ts.diff(xs))((zs,edge) => Triangle(edge.from,edge.to,pt) :: zs)
    }).filterNot(_.sharesVertex(boundary))

    triangles.flatMap(_.toEdges).distinct
  }
```

This can be optimised relatively easily. For example `ts.foldLeft` 
is folding over all the trigangles. It would be possible to use 
something like a Quad tree and cut down the search space. 
This would improve performance and still leave the implementation to a 
few core lines of code (vs. larger implementations for s-hull etc.).
