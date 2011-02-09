package at.ac.tuwien.opt.search
import at.ac.tuwien.opt.file.InstanceReader
import at.ac.tuwien.opt.util.Checker._
import org.hamcrest.core.Is._
import org.junit.Test
import org.junit.Assert._
import org.scalatest.junit.JUnitSuite
import org.scalatest.matchers.MustMatchers
import scala.collection.mutable.ListBuffer

class LocalSearchTest extends JUnitSuite with MustMatchers {

  @Test
  def testCheckAllNodesReached = {
    val instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-05.dat");

    val arcs = List[(Int, Int)]((0, 1), (1, 2), (2, 3))
    assertTrue(checkSolution(instance, arcs))

    val solution = LocalSearch.search(instance, arcs, instance.calculateCosts(arcs))
    println(solution)

    assertThat(solution._2.toSet, is(Set[(Int, Int)]((0, 2), (1, 4))))
    assertTrue(checkSolution(instance, solution._2))

  }

  @Test
  def testLocalSearch = {
    val instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-06.dat");

    val arcs = List[(Int, Int)]((0, 1), (1, 2), (2, 3), (3, 4), (4, 6))
    assertTrue(checkSolution(instance, arcs))

    val solution = LocalSearch.search(instance, arcs, instance.calculateCosts(arcs))
    println(solution)

    assertTrue(checkSolution(instance, solution._2))
    assertThat(solution._2.toSet, is(Set[(Int, Int)]((0, 1), (1, 2), (2, 3), (3, 6))))

  }

  @Test
  def testFindPath = {
    val instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-06.dat");

    val arcs = List[(Int, Int)]((0, 1), (1, 2), (2, 3), (3, 4), (4, 6))
    assertTrue(checkSolution(instance, arcs))

    val path = LocalSearch.findPath(instance, arcs, 3);

    assertThat(path, is(List[(Int, Int)]((0, 1), (1, 2), (2, 3))))
  }

}