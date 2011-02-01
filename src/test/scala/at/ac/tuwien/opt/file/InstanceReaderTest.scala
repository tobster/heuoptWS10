package at.ac.tuwien.opt.file

import org.scalatest.Spec
import org.scalatest.matchers.MustMatchers

class InstanceReaderTest extends Spec with MustMatchers {

  describe("Instance reader") {

    describe("when reading Instance from valid file") {
      val values = InstanceReader.readInstance("target/scala_2.8.1/test-resources/mebp-01.dat");
      it("should contain values") {
        //"296.0".toFloat must be === 296.0
        values.next must be === (296.0, 637.6)
        values.next must be === (394.4, 3.1)
        values.next must be === (.4, 0.6)
      }
    }
  }
}
