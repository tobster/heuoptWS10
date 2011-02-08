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
    var as = new Ant(instance = instance, alpha = 1, beta = 1,tau = Array.fill(instance.size, instance.size){1})
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
    var as = new Ant(instance = instance, alpha = 1, beta = 1,tau = Array.fill(instance.size, instance.size){1})
    assertThat(instance.nodesByDistance(0).toList, is(List(0, 1, 2, 3)))
    assertThat(instance.nodesByDistance(1).toList, is(List(1, 0, 2, 3)))
    assertThat(instance.nodesByDistance(2).toList, is(List(2, 3, 0, 1)))
    assertThat(instance.nodesByDistance(3).toList, is(List(3, 2, 0, 1)))
  }

  @Test def reachedTest() {
    var instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-03.dat")
    var as = new Ant(instance = instance, alpha = 1, beta = 1,tau = Array.fill(instance.size, instance.size){1})
    assertThat(instance.reached(0, 1), is(Set(0, 1)))
    assertThat(instance.reached(0, 2), is(Set(0, 1, 2)))
    assertThat(instance.reached(0, 3), is(Set(0, 1, 2, 3)))
  }

  @Test def reachedTestSameDistance() {
    var instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-02.dat")
    var as = new Ant(instance = instance, alpha = 1, beta = 1,tau = Array.fill(instance.size, instance.size){1})
    println(instance.nodesByDistance(0))
    assertThat(instance.reached(0, 1), is(Set(0, 1, 2)))
    assertThat(instance.reached(0, 2), is(Set(0, 1, 2)))
  }

  @Test def testCalculateCosts() {
    var instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-03.dat")
    var as = new Ant(instance = instance, alpha = 1, beta = 1,tau = Array.fill(instance.size, instance.size){1})
    for (x <- instance.ny; y <- x) {
      println(y)
    }

    var r = List((0, 2), (2, 3))
    assertEquals(instance.ny(0)(2) + instance.ny(2)(3), instance.calculateCosts(r), 0.001)

    println("costs: " + (instance.ny(0)(2) + instance.ny(2)(3)))
  }


  @Test def testVaporizePheromone() {
    var instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-03.dat")
    var as = new Aco(instance = instance, alpha = 1, beta = 1)

    as.vaporizePheromone()

    for (x <- as.tau; y <- x) {
      assertThat(y, is(0.7))
    }
  }

  @Test def testUpdatePheromone() {
    var instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-03.dat")
    var as = new Aco(instance = instance, alpha = 1, beta = 1)

    as.updatePheromone(List((0, 2), (2, 3)), 10)

    assertThat(as.tau(0)(2), is(1.1))
    assertThat(as.tau(2)(3), is(1.1))

    for (x <- 0 to as.tau.length - 1; y <- 0 to as.tau(x).length - 1; if !((x == 0 && y == 2) || (x == 2 && y == 3))) {
      assertThat(as.tau(x)(y), is(1.0))
    }
  }
}