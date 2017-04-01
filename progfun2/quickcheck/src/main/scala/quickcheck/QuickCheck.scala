package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  lazy val genHeap: Gen[H] = oneOf(
    const(empty),
    for {
      e <- arbitrary[Int]
      h <- oneOf(const(this.empty), genHeap)
    } yield insert(e, h)
  )
  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  property("inserting current min again should return the same min") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("inserting 2 elems into empty heap return min of 2 elems") = forAll { (h: H, a:Int, b: Int) =>
    isEmpty(h) ==> {
      findMin(insert(a, insert(b, h))) == Math.min(a, b)
    }
  }

  property("insert and deleteMin on empty heap return empty heap") = forAll { (h: H, x:Int) =>
    isEmpty(h) ==> {
      isEmpty(deleteMin(insert(x, h)))
    }
  }

  property("heap sort") = forAll { (h: H) =>
    def heapSort(h: H, result: List[A]): List[A] =
      if (isEmpty(h)) result
      else heapSort(deleteMin(h), result :+ findMin(h))
    
    val xs = heapSort(h, List())
    xs == xs.sorted
  }

  property("meld empty") = forAll { (h1: H, h2: H) =>
    (isEmpty(h1) && isEmpty(h2)) ==> {
      isEmpty(meld(h1, h2))
    }
  }
}
