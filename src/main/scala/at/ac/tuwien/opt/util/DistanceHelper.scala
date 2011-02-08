package at.ac.tuwien.opt.util

import scala.math._

trait DistanceHelper {
  def d(a: Double, b: Double)(c: Double, d: Double): Double = {
    return pow(pow(a - c, 2) + pow(b - d, 2), 1)
  }
  def distance(a:(Double,Double),b:(Double,Double)): Double =  {
    ((d _).tupled(a)).tupled(b)
  }
}