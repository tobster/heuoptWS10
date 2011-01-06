package at.ac.tuwien.opt.util

import org.scalatest.Spec
import org.scalatest.matchers.MustMatchers
class DistanceHelperTest extends Spec with MustMatchers with DistanceHelper {

  describe("distance of Nodes 0,0 and 0,1") {
    val x = distance(0, 0)(0, 1)
    it("should be 1") {
      x must be === 1.0
    }
  }
  describe("distance of Nodes 0,1 and 0,1") {
    val x = distance(0, 1)(0, 1)
    it("should be 0") {
      x must be === 0
    }
  }
  describe("distance of Nodes 0,0 and 1,1") {
    val x = distance(0, 0)(1, 1)
    it("should be 2.82...") {
      x must be === 2.82842712474619
    }
  }
  describe("distance of Nodes 1,1 and 0,0") {
    val x = distance(1, 1)(0, 0)
    it("should be 2.8...") {
      x must be === 2.82842712474619
    }
  }
  describe("distance of Nodes 5,2 and 7,1") {
    val x = distance(5, 2)(7, 1)
    it("should be 11,180339887") {
      x must be === 11.180339887498949
    }
  }
}