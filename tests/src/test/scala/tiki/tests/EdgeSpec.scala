package tiki.tests

import tiki.Predef._
import org.scalatest.Matchers
import org.scalatest.prop.Checkers
import tiki.{Edge, _}
import tiki.tests.arbitrary.AllArbitrary

/**
  * Test class for `Edge` and `LEdge`
  */
class EdgeSpec  extends TikiSuite with Checkers with Matchers with AllArbitrary {

  test("Edge creates correct to and from vertices") { (x: Int, y: Int) => {
    Edge[Int](x,y) should have ('from (x), 'to (y))
  }}

  test("LEdge creates correct to, from vertices and label") { (x: Int, y: Int, z: Double) => {
    LEdge[Int,Double](Edge(x,y),z) should have ('from (x), 'to (y), 'label (z))
  }}

  test("`lmap` correctly relabels a labelled edge") { (x: Int, y: Int, w: Double, s: String) => {
    LEdge[Int,Double](Edge(x,y),w).lmap[String](_=>s) should have ('label (s), 'from (x), 'to (y))
  }}

  test("`map` correctly maps the vertices of an edge") { (x: Int, y: Int) => {
    Edge[Int](x, y).map[Int](e => Edge(e.from + 1, e.to + 1)) should have('from (x + 1), 'to (x + 1))
  }}

  test("`map` correctly maps the vertices of a labelled edge") { (x: Int, y: Int, z: Double) => {
    LEdge[Int,Double](Edge(x,y),z).map[Int](e=>Edge(e.from+1,e.to+1)) should have ('from(x+1), 'to(x+1), 'label (z))
  }}

  /* Force path for scoverage/codecov. */
  test("`lmap` correctly relabels.") {
    val l0 = LEdge[Int,Double](Edge(1,2),1.0)
    val l1 = l0.lmap[String](_.toString)
    l1.label should be(1.0.toString)
  }

  test("`map` correctly maps labelled edge.") {
    val l0 = LEdge[Int,Double](Edge(1,2),1.0)
    val l1 = l0.map[Int](e=>Edge(e.from+1,e.to+1)).asInstanceOf[LEdge[Int,Double]]
    l1.edge should be (Edge[Int](2,3))
  }

  test("Labelled edge properties are correct.") {
    val l0 = LEdge[Int,Double](Edge(1,2),1.0)
    val (from,to,label) = (l0.from,l0.to,l0.label)
    (from, to, label) should be ((1,2,1.0))
  }

}
