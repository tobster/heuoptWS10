package at.ac.tuwien.opt.util
import at.ac.tuwien.opt.aco.Instance
import scala.collection.mutable.{ BitSet, Queue }

object Checker {
  def checkSolution(instance: Instance, solution: List[(Int, Int)]): Boolean = {
    var result: Boolean = true

    // check if start node is contained in solution.
    if (!solution.exists(_._1 == 0)) {
      println("Solution does not contain source!")
      return false
    }

    // check for duplicate source nodes.
    if (!(solution.map(_._1).toSet.size == solution.size)) {
      println("Duplicate source nodes!")
      return false
    }

    // check for duplicate target nodes.
    if (!(solution.map(_._2).toSet.size == solution.size)) {
      println("Duplicate target nodes!")
      return false
    }

    // check if all nodes are reached.
    val reachable = reachableNodes(instance, solution)
    val diff = Range(0, instance.size).toSet.diff(reachable)

    if (!diff.isEmpty) {
      println("Not all nodes are reachable: " + diff)
      return false
    }

    true
  }

  def reachableNodes(instance: Instance, solution: List[(Int, Int)]): Set[Int] = {
    val queue = new Queue[Int]()
    val reached = new BitSet() + 0
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