package at.ac.tuwien.opt.aco

import at.ac.tuwien.opt.file.InstanceReader
import org.scalatest.matchers.MustMatchers
import org.junit.Test
import org.junit.Assert._
import org.scalatest.junit.JUnitSuite
import org.hamcrest.core.Is._

class ASTest extends JUnitSuite with MustMatchers {


  @Test def testP() {
    var instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-02.dat")
    var as = new Ant(instance = instance.toList, alpha = 1, beta = 1)
    assertEquals(as.p(0, 1), .5, .0001)
    println(as.p(0, 1))
    println(as.p(0, 2))
    assertEquals(as.p(0, 2), .5, .0001)
    assertThat(as.recChoose(0, List(1, 2), 0.0, 0.3), is(Some(1).asInstanceOf[Option[Int]]))
    assertThat(as.recChoose(0, List(1, 2), 0.0, 0.6), is(Some(2).asInstanceOf[Option[Int]]))
    assertThat(as.recChoose(0, List(1, 2), 0.0, 0.5), is(Some(1).asInstanceOf[Option[Int]]))
    assertThat(as.recChoose(0, List(2), 0.5, 0.6), is(Some(2).asInstanceOf[Option[Int]]))
    //assertThat(as.recChoose(0, Nil, 0.5, 0.6), is(None.asInstanceOf[Option[Int]]))


  }

  @Test def nodesByDistanceTest() {
    var instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-03.dat")
    var as = new Ant(instance = instance.toList, alpha = 1, beta = 1)
    assertThat(as.nodesByDistance(0).toList, is(List(0, 1, 2, 3)))
    assertThat(as.nodesByDistance(1).toList, is(List(1, 0, 2, 3)))
    assertThat(as.nodesByDistance(2).toList, is(List(2, 3, 0, 1)))
    assertThat(as.nodesByDistance(3).toList, is(List(3, 2, 0, 1)))
  }

  @Test def reachedTest() {
    var instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-03.dat")
    var as = new Ant(instance = instance.toList, alpha = 1, beta = 1)
    assertThat(as.reached(0, 1), is(List(0, 1).asInstanceOf[TraversableOnce[Int]]))
    assertThat(as.reached(0, 2), is(List(0, 1, 2).asInstanceOf[TraversableOnce[Int]]))
    assertThat(as.reached(0, 3), is(List(0, 1, 2, 3).asInstanceOf[TraversableOnce[Int]]))
  }

  @Test def reachedTestSameDistance() {
    var instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-02.dat")
    var as = new Ant(instance = instance.toList, alpha = 1, beta = 1)
    println(as.nodesByDistance(0))
    assertThat(as.reached(0, 1), is(List(0, 1, 2).asInstanceOf[TraversableOnce[Int]]))
    assertThat(as.reached(0, 2), is(List(0, 1, 2).asInstanceOf[TraversableOnce[Int]]))
  }

  @Test def testCalculateCosts() {
    var instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-03.dat")
    var as = new Ant(instance = instance.toList, alpha = 1, beta = 1)
    for (x <- as.ny; y <- x) {
      println(y)
    }

    var r = List((0, 2), (2, 3))
    assertEquals(as.ny(0)(2) + as.ny(2)(3), as.calculateCosts(r), 0.001)

    println("costs: " + (as.ny(0)(2) + as.ny(2)(3)))
  }


  @Test def testVaporizePheromone() {
    var instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-03.dat")
    var as = new Ant(instance = instance.toList, alpha = 1, beta = 1)

    as.vaporizePheromone()

    for (x <- as.tau; y <- x) {
      assertThat(y, is(0.7))
    }
  }

  @Test def testUpdatePheromone() {
    var instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-03.dat")
    var as = new Ant(instance = instance.toList, alpha = 1, beta = 1)

    as.updatePheromone(List((0, 2), (2, 3)), 10)

    assertThat(as.tau(0)(2), is(1.1))
    assertThat(as.tau(2)(3), is(1.1))

    for (x <- 0 to as.tau.length - 1; y <- 0 to as.tau(x).length - 1; if !((x == 0 && y == 2) || (x == 2 && y == 3))) {
      assertThat(as.tau(x)(y), is(1.0))
    }
  }
}