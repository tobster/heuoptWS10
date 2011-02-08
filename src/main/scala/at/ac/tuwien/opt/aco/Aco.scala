package at.ac.tuwien.opt.aco

import scala.math._

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