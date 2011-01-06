package at.ac.tuwien.util

import scala.math._

trait DistanceHelper {
  def distance(a: Double, b: Double)(c: Double, d: Double): Double = {
    return pow(pow(a - c, 2) + pow(b - d, 2), 1.5)
  }
}