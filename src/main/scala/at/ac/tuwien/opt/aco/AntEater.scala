package at.ac.tuwien.opt.aco

import at.ac.tuwien.opt.file.InstanceReader
import at.ac.tuwien.opt.util.Checker

object AntEater {

  def main(args: Array[String]) {
    var instance = InstanceReader.readInstance("target/scala_2.8.1/resources/mebp/mebp-03.dat")
    var as = new Aco(instance = instance, alpha = 0, beta = 0, rho = 0.5, tauMax = 2, tauMin = 0.5, initialPheromone = 2)

    val result = as.solve(1000, 1000)
    println("final result")
    println(result._1)
    result._2.foreach(println(_))

    println("checking result: ")
    println(Checker.checkSolution(instance, result._2))
  }

}