package at.ac.tuwien.opt.aco
import at.ac.tuwien.opt.file.InstanceReader
import at.ac.tuwien.opt.util.Checker
import at.ac.tuwien.opt.search.LocalSearch
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics
import java.io.FileWriter
import java.util.Date

object AntEater {

  def main(args: Array[String]) {
    val fw = new FileWriter("results.txt", true)
    for (instanceFile <- List("target/scala_2.8.1/resources/mebp/mebp-06.dat","target/scala_2.8.1/resources/mebp/mebp-08.dat","target/scala_2.8.1/resources/mebp/mebp-10.dat")) {
      var instance = InstanceReader.readInstance(instanceFile)
      var as = new Aco(instance = instance, alpha = 0.5, beta = 0.5, rho = 0.5, tauMax = 2, tauMin = 0.5, initialPheromone = 2)
      var stat = new DescriptiveStatistics()
      for (run <- 1 to 10) {
        var result = as.solve(iterations = 500, numberOfAnts = 100)

        println("aco result")
        println(result._1)
        result._2.foreach(println(_))

        result = LocalSearch.search(instance, result._2, result._1)

        println("local search result")
        println(result._1)
        result._2.foreach(println(_))

        println("checking result: ")
        if (Checker.checkSolution(instance, result._2)) {
          println("result ok")
          stat.addValue(result._1)
        } else {
          println("result broken")
        }
      }
      fw.write(List("\n", new Date(), "\t",
        instanceFile, " ", as, "\nN: ", stat.getN, "\tmean: ", stat.getMean,
        "\tmin: ", stat.getMin, "\tmax: ", stat.getMax,
        "\tStandardDeviation: ", stat.getStandardDeviation,
        "\tVariance: ", stat.getVariance).mkString);
      fw.flush()
    }
    fw.close()

  }

}