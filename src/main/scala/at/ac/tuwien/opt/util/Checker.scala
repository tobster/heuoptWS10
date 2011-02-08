package at.ac.tuwien.opt.util

import scala.collection.mutable.{BitSet, Queue}
import at.ac.tuwien.opt.aco.Instance

object Checker {
  def checkSolution(instance: Instance, solution: List[(Int, Int)]): Boolean = {
    var result: Boolean = true

    // check if start node is contained in solution.
    result &&= solution.exists(_._1 == 0)

    // check for duplicate source nodes.
    result &&= solution.map(_._1).toSet.size == solution.size

    // check for duplicate target nodes.
    result &&= solution.map(_._2).toSet.size == solution.size

    // check if all nodes are reached.
    result &&= reachableNodes(instance, solution).equals(Range(0, instance.size).toSet)

    result
  }

  def reachableNodes(instance: Instance, solution: List[(Int, Int)]): Set[Int] = {
    val queue = new Queue[Int]()
    val reached = new BitSet(0)
    val sources = solution.map(_._1).toSet

    // initial source is 0.
    queue += 0

    while (!queue.isEmpty) {
      val source = queue.dequeue
      val target = solution.find(_._1 == source).get._2

      val reachedBySource = instance.reached(source, target)

      queue ++= reachedBySource.filter(a => sources.contains(a) && !reached.contains(a))
      reached ++= reachedBySource
    }
    reached.toSet
  }
}