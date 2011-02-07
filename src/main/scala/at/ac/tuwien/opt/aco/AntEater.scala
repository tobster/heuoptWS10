package at.ac.tuwien.opt.aco

import at.ac.tuwien.opt.file.InstanceReader

object AntEater {

  def main(args: Array[String]) {
    var instance = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-03.dat")
    var as = new Ant(instance = instance.toList, alpha = 0.5, beta = 0.5)

    val result = as.solve(10)

    println(result._1)
    result._2.foreach(println(_))
  }

}