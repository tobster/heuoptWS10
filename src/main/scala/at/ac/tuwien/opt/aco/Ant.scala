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
      buffer :+ (source, target)
    }
    // Reset neighborhood and visited.
    neighbourhood = BitSet() ++ Range(1, instance.size)
    visited = BitSet(0)
    buffer.toList
  }
}