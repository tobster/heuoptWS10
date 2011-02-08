package at.ac.tuwien.opt.search
import at.ac.tuwien.opt.aco.Instance
import at.ac.tuwien.opt.util.DistanceHelper
import scala.collection.mutable.ListBuffer

trait LocalSearch extends DistanceHelper {

  def search(instance: Instance, solution: List[(Int, Int)], cost: Double): (Double, List[(Int, Int)]) = {
    val sources = solution.map(_._1).toSet
    val nodes = Range(1, instance.size).toSet
    for (s <- sources; n <- nodes; if s != n) {
      val oldarc = solution.find(_._1 == s).get
      if (distance(instance(s), instance(n)) > distance(instance(oldarc._1), instance(oldarc._2))) {

        var buffer = new ListBuffer ++ solution.filter(t => t._1 != s)
        buffer :+ (s, n)
        val reachedBySN = instance.reached(s, n)
        
        // find arcs which have target in the expanded area.
        val arcsIntoNewArea = solution.filter((x: (Int, Int)) => reachedBySN(x._2))
        
        // calculate sets of nodes of areas which are to contract. 
        val diff = arcsIntoNewArea.map(arc => instance.reached(arc._1, arc._2).diff(reachedBySN))
        
        // in each set look for the node which is farthest away.
        
        // contract area.
       
      }
    }
    (0, Nil)
  }
}