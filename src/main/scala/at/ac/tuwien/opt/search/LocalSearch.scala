package at.ac.tuwien.opt.search
import at.ac.tuwien.opt.aco.Instance
import at.ac.tuwien.opt.util.DistanceHelper
import scala.collection.mutable.{ ListBuffer, Queue, BitSet, HashMap }

object LocalSearch extends DistanceHelper {

  def search(instance: Instance, solution: List[(Int, Int)], cost: Double): (Double, List[(Int, Int)]) = {
    var minSolution = solution
    var minCost = cost

    val nodes = Range(1, instance.size).toSet

    for (s <- solution; n <- nodes; if s._1 != n) {
      if (distance(instance(s._1), instance(n)) > distance(instance(s._1), instance(s._2))) {

        // remove old arc.
        var buffer = new ListBuffer ++ solution.filter(t => t._1 != s._1)
        // add new arc.
        buffer.append((s._1, n))

        // calculate expanded area.
        val reachedBySN: Set[Int] = instance.reached(s._1, n)

        println("reached by expansion from " + s + " to " + n + ": " + reachedBySN)

        // find path to expanding node
        val path = findPath(instance, solution, s._1)

        // find arcs which have target in the expanded area.
        val arcsIntoNewArea = solution.filter((x: (Int, Int)) => reachedBySN(x._2) && x._1 != 0 && !path.contains(x))

        println("arcsIntoNewArea: " + arcsIntoNewArea)

        // calculate sets of nodes of areas which are to contract. 
        val diff: List[Set[Int]] = arcsIntoNewArea.map(arc => instance.reached(arc._1, arc._2).diff(reachedBySN))

        println("diff " + diff)

        // in each set look for the node which is farthest away.
        val newTargets: Seq[Seq[Int]] = diff.view.zipWithIndex.map(_ match { case (nodes: Set[Int], i: Int) => nodes.toSeq.sortWith(instance.sort(arcsIntoNewArea(i)._1)) })

        println("newTargets: " + newTargets)

        // delete old arcs reaching into expanded area.
        buffer = buffer.diff(arcsIntoNewArea)

        // contract areas.
        arcsIntoNewArea.zipWithIndex.map(_ match { case (u: (Int, Int), i: Int) if !newTargets(i).isEmpty => buffer.append((u._1, newTargets(i).last)) case _ => })

        val newSolution = buffer.toList
        val newCost = instance.calculateCosts(newSolution)

        if (newCost < minCost) {
          minCost = newCost
          minSolution = newSolution
        }
      }
    }
    (minCost, minSolution)
  }

  def findPath(instance: Instance, solution: List[(Int, Int)], t: Int): List[(Int, Int)] = {
    val queue = new Queue[Int]()
    val reached = new BitSet() + 0
    val sources = solution.map(_._1).toSet

    val map = new HashMap[Int, Int]()

    // initial source is 0.
    queue += 0

    while (!queue.isEmpty) {
      var source = queue.dequeue
      var target = solution.find(_._1 == source).get._2

      val reachedBySource = instance.reached(source, target)

      println(reachedBySource)

      // fill map.
      reachedBySource.foreach(a => if (a != source && !map.contains(a)) map.put(a, source))

      println(map)

      if (reachedBySource(t)) {
        // build path.
        target = t
        var path: List[(Int, Int)] = List((source, target))
        while (source != 0) {
          target = source
          // fetch next arc.
          source = map(source)
          path = (source, target) :: path
        }
        return path
      }

      queue ++= reachedBySource.filter(a => sources.contains(a) && !reached.contains(a))
      reached ++= reachedBySource

    }
    Nil
  }
}