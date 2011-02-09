package at.ac.tuwien.opt.aco
import at.ac.tuwien.opt.file.InstanceReader
import at.ac.tuwien.opt.util.Checker
import at.ac.tuwien.opt.search.LocalSearch

object AntEater {

  def main(args: Array[String]) {
    var instance = InstanceReader.readInstance("target/scala_2.8.1/resources/mebp/mebp-02.dat")
    var as = new Aco(instance = instance, alpha = 0, beta = 0, rho = 0.5, tauMax = 2, tauMin = 0.5, initialPheromone = 2)

    var result = as.solve(1000, 100)
    println("aco result")
    println(result._1)
    result._2.foreach(println(_))

    result = LocalSearch.search(instance, result._2, result._1)

    println("local search result")
    println(result._1)
    result._2.foreach(println(_))

    
    println("checking result: ")
    println(Checker.checkSolution(instance, result._2))

  }

}