package at.ac.tuwien.util

import scala.math._

trait DistanceHelper {
  def distance(a: (Double, Double), b: (Double, Double)): Double = {
    return pow(pow(a._1 - b._1, 2) + pow(a._2 - b._2, 2), 1.5)
  }
}