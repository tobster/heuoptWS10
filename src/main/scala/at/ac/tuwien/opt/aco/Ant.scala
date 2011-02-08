package at.ac.tuwien.opt.aco

import scala.math._
import at.ac.tuwien.opt.util.DistanceHelper
import scala.util.Random
import collection.mutable.{ListBuffer, BitSet}

class Ant(val instance: Instance,
          val tau: Array[Array[Double]],
          alpha: Double = .5,
          beta: Double = .5)
  extends DistanceHelper {

  def recChoose(source: Int, xs: List[Int], prob: Double, border: Double): Option[Int] = {
    val tmp: Double = prob + p(source, xs.head)
    xs match {
      case Nil => None
      case x :: xs if tmp >= border => Some(x)
      case x if x.tail.isEmpty => Some(x.head)
      case _ => recChoose(source, xs.tail, tmp, border)
    }
  }

  def chooseTarget(source: Int): Option[Int] = {
    recChoose(source, neighbourhood.toList, 0, Random.nextDouble)
  }

  def chooseSource(): Int = visited.toList(Random.nextInt(visited.size))

  // unvisited neighbours.
  var neighbourhood: BitSet = BitSet() ++ Range(1, instance.size)
  // the visited cities.
  var visited: BitSet = BitSet(0)

  def p(i: Int, j: Int): Double = {
    pow(tau(i)(j), alpha) * pow(instance.ny(i)(j), beta) /
      (0.0 /: neighbourhood)((total, l) => total + pow(tau(i)(l), alpha) * pow(instance.ny(i)(l), beta))
  }

  def run(): List[(Int, Int)] = {
    var buffer = new ListBuffer[(Int, Int)]
    while (!neighbourhood.isEmpty) {
      val source: Int = chooseSource()
      val target: Int = chooseTarget(source).get
      val r: BitSet = new BitSet() ++ instance.reached(source, target)

      visited ++= r
      neighbourhood --= r

      buffer = buffer.filter(t => t._1 != source)
      buffer = buffer :+ (source, target)
    }
    // Reset neighborhood and visited.
    neighbourhood = BitSet() ++ Range(1, instance.size)
    visited = BitSet(0)
    buffer.toList
  }
}

class Aco(val instance: Instance,
          initialPheromone: Double = 1.0,
          rho: Double = 0.3,
          alpha: Double = .5,
          beta: Double = .5,
          tauMax: Double = Double.MaxValue,
          tauMin: Double = 0) {
  //pheromones
  var tau: Array[Array[Double]] = Array.fill(instance.size, instance.size){ initialPheromone } 

  def updatePheromone(result: List[(Int, Int)], cost: Double) = {
    result.foreach((t) => tau(t._1)(t._2) = min(tau(t._1)(t._2) * 1 + 1 / cost, tauMax))
  }

  def vaporizePheromone() = {
    tau = tau.map(t => t.map(u => max((1 - rho) * u, tauMin)))
  }

  def solve(iterations: Int, numberOfAnts: Int): (Double, List[(Int, Int)]) = {
    var min: List[(Int, Int)] = Nil
    var minCosts: Double = Double.PositiveInfinity
    for (t <- 0 to iterations) {
      var result: List[(Int, Int)] = Nil
      for (i <- 0 to numberOfAnts) {
        val ant = new Ant(instance, tau, alpha, beta)
        result = ant.run()
        val cost = instance.calculateCosts(result)
        if (cost < minCosts) {
          minCosts = cost;
          min = result;
          println("iteration: " + t + "\tant: " + i + "\t\tcost: " + minCosts + "\t\tresult: " + result)
        }
      }
      updatePheromone(min, minCosts)
      vaporizePheromone()
    }
    return (minCosts, min)
  }
}