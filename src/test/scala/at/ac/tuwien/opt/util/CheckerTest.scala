package at.ac.tuwien.opt.util


import at.ac.tuwien.opt.file.InstanceReader
import org.scalatest.matchers.MustMatchers
import org.junit.Test
import org.scalatest.junit.JUnitSuite
import org.junit.Assert._
import org.hamcrest.core.Is._

class CheckerTest extends JUnitSuite with MustMatchers {

  @Test def testCheckAllNodesReached = {
    val instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-02.dat");
    assertTrue(Checker.checkSolution(instance, List[(Int, Int)]((0, 2))))
  }

  @Test def testCheckUnreachedNodes = {
    val instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-03.dat");
    assertFalse(Checker.checkSolution(instance, List[(Int, Int)]((0, 1))))
  }

  @Test def testCheckStartNodeMissing = {
    val instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-02.dat");
    assertFalse(Checker.checkSolution(instance, List[(Int, Int)]((1, 2))))
  }

  @Test def testCheckDoubleSourceNode = {
    val instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-02.dat");
    assertFalse(Checker.checkSolution(instance, List[(Int, Int)]((0, 2), (0, 1))))
  }

  @Test def testCheckDoubleTargetNode = {
    val instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-02.dat");
    assertFalse(Checker.checkSolution(instance, List[(Int, Int)]((0, 2), (1, 2))))
  }

  @Test def testCheckReachability = {
    val instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-04.dat");
    assertTrue(Checker.checkSolution(instance, List[(Int, Int)]((3, 2), (0, 1), (1, 4))))
  }

  @Test def testCheckReachabilityFail = {
    val instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-04.dat");
    assertFalse(Checker.checkSolution(instance, List[(Int, Int)]((3, 2), (0, 1))))
  }

  @Test def testReached = {
     val instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-04.dat");
     assertThat(instance.reached(0,1), is(Set(0,1,3)))
  }

  @Test def testReachedNodes = {
    val instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-04.dat");
    assertThat(Checker.reachableNodes(instance, List[(Int, Int)]((3, 2), (0, 1))), is(Set[Int](0,1,2,3)))
  }
  @Test def testReachedNodes2 = {
    val instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-04.dat");
    assertThat(Checker.reachableNodes(instance, List[(Int, Int)]((3, 2), (0, 1), (1,4))), is(Set[Int](0,1,2,3,4)))
  }
}