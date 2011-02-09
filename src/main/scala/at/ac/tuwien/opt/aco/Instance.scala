package at.ac.tuwien.opt.aco
import at.ac.tuwien.opt.util.DistanceHelper

class Instance(val points: List[(Double, Double)]) extends DistanceHelper {

  def size = points.size

  //distances
  val ny: Array[Array[Double]] = Array.tabulate(size, size)((i: Int, j: Int) => distance(points(i), points(j)))

  def apply(i: Int): (Double, Double) = {
    points(i)
  }

  val nodesByDistance: Array[Seq[Int]] = Array.tabulate(size)((i: Int) => Seq.range(0, size).sortWith(sort(i)))

  def reached(source: Int, target: Int): Set[Int] = {
    nodesByDistance(source).takeWhile((current: Int) => ny(source)(current) - (ny(source)(target)) < 0.0001).toSet
  }

  def calculateCosts(result: List[(Int, Int)]): Double = {
    result.foldLeft(0.0)((v: Double, c: (Int, Int)) => v + ny(c._1)(c._2))
  }

  def sort(i: Int)(a: Int, b: Int): Boolean = {
    ny(i)(a) < ny(i)(b)
  }

}