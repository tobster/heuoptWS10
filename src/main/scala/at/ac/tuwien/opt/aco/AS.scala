package at.ac.tuwien.opt.aco

import scala.math._
import at.ac.tuwien.opt.util.DistanceHelper
import scala.util.Random
import collection.mutable.{ListBuffer, BitSet}

class Ant(instance: List[(Double, Double)],
          alpha: Double = .5,
          beta: Double = .5,
          initialPheromone: Double = 1.0,
          rho: Double = 0.3)
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
  var tau: Array[Array[Double]] = Array.fill(instance.size, instance.size) {
    initialPheromone
  }
  //distances
  val ny: Array[Array[Double]] = Array.tabulate(instance.size, instance.size)((i: Int, j: Int) => distance(instance(i), instance(j)))
  // unvisited neighbours.
  var neighbourhood: BitSet = BitSet() ++ Range(1, instance.size)
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
    var min: List[(Int, Int)] = Nil
    var minCosts: Double = Double.PositiveInfinity
    for (t <- 0 to iterations) {
      var buffer = new ListBuffer[(Int, Int)]
      while (!neighbourhood.isEmpty) {
        val source: Int = chooseSource()
        val target: Int = chooseTarget(source).get
        val r: BitSet = new BitSet() ++ reached(source, target)

        visited ++= r
        neighbourhood --= r

        buffer = buffer.filter(t => t._1 != source)
        buffer = buffer :+ (source, target)
      }
      val result = buffer.toList
      val cost = calculateCosts(result)
      updatePheromone(result, cost)
      vaporizePheromone()

      if (cost < minCosts) {
        minCosts = cost;
        min = result;
      }

      // Reset neighborhood and visited.
      neighbourhood = BitSet() ++ Range(1, instance.size)
      visited = BitSet(0)
    }
    return (minCosts, min)
  }


  def calculateCosts(result: List[(Int, Int)]): Double = {
    result.foldLeft(0.0)((v: Double, c: (Int, Int)) => v + ny(c._1)(c._2))
  }

  def updatePheromone(result: List[(Int, Int)], cost: Double) = {
    result.foreach((t) => tau(t._1)(t._2) += 1 / cost)
  }

  def vaporizePheromone() = {
    tau = tau.map(t => t.map(u => (1 - rho) * u))
  }
}