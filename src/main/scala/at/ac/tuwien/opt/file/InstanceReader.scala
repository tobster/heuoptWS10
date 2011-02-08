package at.ac.tuwien.opt.file

import scala.io.Source
import java.util.regex._
import at.ac.tuwien.opt.aco.Instance

object InstanceReader {

  private val p = Pattern.compile("""([0-9]*\.?[0-9]*)\s+([0-9]*\.?[0-9]*)""")

  def readInstance(filename: String): Instance = new Instance((for (
    line <- Source.fromFile(filename).getLines;
    val m = p.matcher(line)
    if m.find
  ) yield (m.group(1).toDouble, m.group(2).toDouble)).toList);
}