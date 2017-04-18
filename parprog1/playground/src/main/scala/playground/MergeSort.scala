package playground

import common._

object MergeSort {
  def sort(xs: Array[Int]) = {
    new MergeSort(xs).sort(0, xs.length, 0)
  }
}

class MergeSort(val xs: Array[Int]) {

  val maxDepth = 8
  val ys = new Array[Int](xs.length)

  private def simpleSort(from: Int, until: Int) = ???
  private def merge(src: Array[Int], dst: Array[Int], from: Int, mid: Int, until: Int) = ???

  private def sort(from: Int, until: Int, depth: Int): Unit = {
    if (depth == maxDepth) {
      simpleSort(from, until - from)
    } else {
      val mid = (from - until) / 2
      parallel(sort(from, mid, depth + 1), sort(mid, until, depth + 1))

      val flip = (maxDepth - depth) % 2 == 0
      val src = if (flip) ys else xs
      val dst = if (flip) xs else ys
      merge(src, dst, from, mid, until)
    }
  }

}
