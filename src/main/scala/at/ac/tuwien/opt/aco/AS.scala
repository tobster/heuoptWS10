package at.ac.tuwien.opt.aco

import scala.math._
import at.ac.tuwien.opt.util.DistanceHelper
import scala.util.Random
import scala.collection.mutable.BitSet

class Ant(instance: List[(Double, Double)],
          alpha: Double = .5,
          beta: Double = .5,
          initialPheromone: Double = 1.0)
  extends DistanceHelper {

  def recChoose(source: Int, xs: List[Int], prob: Double, border: Double): Option[Int] = {
    val tmp: Double = prob + p(source, xs.head)
    xs match {
      case Nil => None
      case x :: xs if tmp >= border => Some(x)
      case _ => recChoose(source, xs.tail, tmp, border)
    }
  }

  def chooseTarget(source: Int): Option[Int] = {
    recChoose(source, neighbourhood.toList, 0, Random.nextDouble)
  }

  def chooseSource(): Int = visited.toList(Random.nextInt(visited.size))

  //pheromones
  val tau: Array[Array[Double]] = Array.fill(instance.size, instance.size) {
    initialPheromone
  }
  //distances
  val ny: Array[Array[Double]] = Array.tabulate(instance.size, instance.size)((i: Int, j: Int) => distance(instance(i), instance(j)))
  // unvisited neighbours.
  var neighbourhood: BitSet = new BitSet(List.range(1, instance.size))
  // the visited cities.
  var visited: BitSet = BitSet(0)

  val nodesByDistance: Array[Seq[Int]] = Array.tabulate(instance.size)((i: Int) =>
    Seq.range(0, instance.size).sorted(new Ordering[Int] {
      override def compare(x: Int, y: Int): Int = {
        ny(i)(x) - (ny(i)(y)) match {
          case d if d.abs <= 0.001 => 0
          case d if d > 0 => 1
          case _ => -1
        }
      }
    })
  )

  def p(i: Int, j: Int): Double = {
    pow(tau(i)(j), alpha) * pow(ny(i)(j), beta) /
      (0.0 /: neighbourhood)((total, l) => total + pow(tau(i)(l), alpha) * pow(ny(i)(l), beta))
  }

  def reached(source: Int, target: Int): TraversableOnce[Int] = {
    nodesByDistance(source).takeWhile((current: Int) => ny(source)(current) - (ny(source)(target)) < 0.001)
  }

  def solve(iterations: Int): (Double, List[(Int, Int)]) = {
    for (t <- 0 to iterations) {
      while (!neighbourhood.isEmpty) {
        val source: Int = chooseSource()
        val target: Int = chooseTarget(source).get
        val r: TraversableOnce[Int] = reached(source, target)
        visited ++= r
        neighbourhood --= r
      }
    }
    return (.0, Nil)
  }

}