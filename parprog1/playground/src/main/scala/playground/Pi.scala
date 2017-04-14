package playground

import common._
import scala.util.Random

/**
  * Programmed by dmitriipetukhov on 4/15/17.
  */
object Pi {

  private def mcCount(attempts: Int): Int = {
    val rndX = new Random
    val rndY = new Random
    var hits = 0

    for (i <- 0 until attempts) {
      val x = rndX.nextDouble() // [0, 1]
      val y = rndY.nextDouble() // [0, 1] => (x, y) is inside a 1 x 1 square
      if (x * x + y * y < 1) hits += 1 // if we hit inside the circle
    }

    hits
  }

  def MonteCarloPiSeq(attempts: Int): Double = 4.0 * mcCount(attempts) / attempts

  def MonteCarloPiPar(attempts: Int): Double = {
    val ((pi1, pi2), (pi3, pi4)) = parallel(
        parallel(mcCount(attempts / 4), mcCount(attempts / 4)),
        parallel(mcCount(attempts / 4), mcCount(attempts / 4))
    )
    4.0 * (pi1 + pi2 + pi3 + pi4) / attempts
  }

  def MonteCarloPiTasks(attempts: Int): Double = {
    val pi1 = task {mcCount(attempts / 4)}
    val pi2 = task {mcCount(attempts / 4)}
    val pi3 = task {mcCount(attempts / 4)}
    val pi4 = task {mcCount(attempts / 4)}
    4.0 * (pi1.join() + pi2.join() + pi3.join() + pi4.join()) / attempts
  }
}
