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
    assertThat(as.nodesByDistance(0).toList, is(List(0,1,2,3)));
    assertThat(as.nodesByDistance(1).toList, is(List(1,0,2,3)));
    assertThat(as.nodesByDistance(2).toList, is(List(2,3,0,1)));
    assertThat(as.nodesByDistance(3).toList, is(List(3,2,0,1)));

  }


}
