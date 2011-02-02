package at.ac.tuwien.opt.aco

import scala.math._
import at.ac.tuwien.opt.util.DistanceHelper
import scala.util.Random

/**
 * Created by IntelliJ IDEA.
 * User: tobi
 * Date: 16.01.11
 * Time: 22:57
 * To change this template use File | Settings | File Templates.
 */

class AS(val instance: List[(Double, Double)],
         alpha: Double = .5,
         beta: Double = .5,
         initialPheromone: Double = 1.0)
  extends DistanceHelper {
  type Matrix = Array[Array[Double]]
  //pheromones
  val tau: Matrix = Array.fill(instance.size, instance.size) {
    initialPheromone
  }
  //distances
  val ny: Matrix = Array.tabulate(instance.size, instance.size)((i: Int, j: Int) => distance(instance(i), instance(j)))

  def chooseTarget(source: Int): Option[Int] = {
    val r = Random.nextDouble

    def recChoose(xs: List[Int], prob: Double): Option[Int] = {
      val tmp:Double = prob + p(source, xs.head)
      xs match {
        case Nil => None
        case x :: xs if tmp >= r => Some(x)
        case _ => recChoose(xs.tail, tmp)
      }
    }
    recChoose(neighbourhood, 0)
  }

  def chooseSource(): Int = visited(Random.nextInt(visited.size))

  // unvisited neighbours.
  val neighbourhood: List[Int] = List.range(1, instance.size)
  // the visited cities.
  val visited: List[Int] = List(0)

  def p(i: Int, j: Int): Double = {
    pow(tau(i)(j), alpha) * pow(ny(i)(j), beta) /
      (0.0 /: neighbourhood)((total, l) => total + pow(tau(i)(l), alpha) * pow(ny(i)(l), beta))
  }
}