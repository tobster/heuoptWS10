package at.ac.tuwien.opt.aco

import at.ac.tuwien.opt.file.InstanceReader
import org.scalatest.Spec
import org.scalatest.matchers.MustMatchers

class ASTest extends Spec with MustMatchers {

  describe("ant system") {
  var instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-02.dat")
    describe("p value") {
      var as = new AS(instance = instance.toList,alpha = 1,beta = 1)
      print(as.ny )
      it("should be 1") {
        println(as.p(0,0))
        println(as.p(0,1))
        println(as.p(1,1))
        println(as.p(0,2))
        println(as.p(2,0))
        println(as.p(1,2))
        println(as.tau(0)(0))
        println(as.ny(0)(1))
        println("ny")
        as.ny.foreach(x => x.foreach(y => println(y)))
        println("instance")
        println(as.instance)
        // as.p(0, 1) must be === 1.0
//        as.p(0, 2) must be === 1.0
//        as.p(1, 2) must be === 1.0
//        as.p(2, 1) must be === 1.0
//        as.p(2, 1) must be === 1.0
      }
    }
  }
}