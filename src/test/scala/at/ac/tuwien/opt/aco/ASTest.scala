package at.ac.tuwien.opt.aco

import at.ac.tuwien.opt.file.InstanceReader
import org.scalatest.matchers.MustMatchers
import org.junit.Test
import org.junit.Assert._
import org.scalatest.junit.{JUnitSuite, AssertionsForJUnit}

class ASTest extends JUnitSuite with MustMatchers {

  var instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-02.dat")
  var as = new AS(instance = instance.toList,alpha = 1,beta = 1)

  @Test def testP() {



  }


}
