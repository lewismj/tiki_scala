package tiki.tests.arbitrary


import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import tiki.{Edge, EdgeLike}
import tiki.Predef._

trait ArbitraryEdgeList extends ArbitrarySet {

  def edgeList[A: Arbitrary] : Gen[List[EdgeLike[A]]] = for {
    xs <- arbitrary[Set[A]]
    if xs.nonEmpty
  } yield {
    xs.iterator.sliding(2).foldLeft(List.empty[EdgeLike[A]])((acc, v) => {
      Edge(v.head, v.last) :: acc
    })
  }

  implicit def arbitraryEdgeList[A:Arbitrary]: Arbitrary[List[EdgeLike[A]]] = Arbitrary(edgeList)
}
