package tiki
package instances

import cats.Show


trait WeightedEdgeInstances {

  final class WeightDef[A](e: Edge[A]) {
    def :#(w: Double): WeightedEdge[A] = new WeightedEdge[A](e,w)
  }

  implicit def anyToWEdge[A](e: Edge[A]): WeightDef[A] = new WeightDef[A](e)

  implicit def weightedToEdge[A](s: Stream[WeightedEdge[A]]): Stream[Edge[A]] = s.map(_.edge)

  implicit def catsStdShowForWEdge[A]: Show[WeightedEdge[A]]
    = (f: WeightedEdge[A]) => s"${f.from} --> ${f.to} :# ${f.weight}"
}

