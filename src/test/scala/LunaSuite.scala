import org.scalatest.funsuite.AnyFunSuite


class LunaSuite extends AnyFunSuite {
  test("returnHi() returns Hi") {
    assert(Luna.returnHi() == "Hi")
  }
}
