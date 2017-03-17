package tiki.tests

import tiki.Predef._
import org.scalatest.Matchers
import org.scalatest.prop.Checkers
import tiki._
import tiki.tests.arbitrary.AllArbitrary


/**
  * Test utility functions.
  */
class TikiSpec extends TikiSuite with Checkers with Matchers with AllArbitrary {

  test("`reverse` of an edge should swap the two and from vertices") { (x:Int, y:Int) => {
     reverse(Edge[Int](x,y)) should have ('from (y), 'to (x))
  }}

  test("`reverse` of a labelled edge should swap the vertices") { (x: Int, y: Int, z: Double) => {
    reverse(LEdge[Int,Double](Edge(x,y),z)) should have ('from (y), 'to (x), 'label (z))
  }}

  /*
  test("`reverseAll` should reverse all the edges in a set of edges") {
    ???
  }
  */

}
