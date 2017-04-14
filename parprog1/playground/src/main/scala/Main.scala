object Main {

  private var idCount = 0

  private val x = new AnyRef {}
  private var idCountSafe = 0

  def getUniqueId = {
    idCount += 1
    idCount
  }

  def getUniqueIdSafe = x.synchronized {
    idCountSafe -= 1
    idCountSafe
  }

  def genIntsAndPrint(genInts: => Int) = {
    val uids = for (i <- 1 to 10) yield genInts
    println(uids)
  }

  def startThread(f: => Unit) = {
    val t = new Thread {
      override def run() = f
    }
    t.start()
    t
  }

  def doNTimes(n: Int, f: => Unit) = for (i <- 1 to n) f

  def main(args: Array[String]): Unit = {
    val t1 = new HelloThread
    val t2 = new HelloThread
    t1.start()
    t2.start()
    t1.join() // wait for t1 completion
    t2.join() // wait for t2 completion
    println("Hello world!")

    // -----------

    // example of non atomicity
    // can produce:
    // Vector(1, 2, 3, 5, 7, 9, 11, 12, 13, 15)
    // Vector(1, 2, 4, 6, 8, 10, 11, 12, 14, 16)
    startThread(genIntsAndPrint(getUniqueId))
    startThread(genIntsAndPrint(getUniqueId))

    // synchronized (using monitor)
    startThread(genIntsAndPrint(getUniqueIdSafe))
    startThread(genIntsAndPrint(getUniqueIdSafe))

    // -----------
    // example of deadlocks:
    //
    // val a1 = new Account(10000)
    // val a2 = new Account(20000)
    //
    // val a12 = startThread({doNTimes(10, a1.transfer(a2, 1))})
    // val a21 = startThread({doNTimes(10, a2.transfer(a1, 1))})
    // a12.join() // a12 and a21 can be never finished (thanks deadlocks!)
    // a21.join() // that is why join blocks the main thread

    // one solution – always acquire resources in the same order
    val a1 = new SafeAccount(10000)
    val a2 = new SafeAccount(20000)

    val a12 = startThread({doNTimes(10, a1.transfer(a2, 1))})
    val a21 = startThread({doNTimes(10, a2.transfer(a1, 1))})
    a12.join() // a12 and a21 can be never finished (thanks deadlocks!)
    a21.join() // that is why join blocks the main thread

    //  Memory model is a set of rules that describes how threads interact when
    //    accessing shared memory.
    //
    //  Java Memory Model – the memory model for the JVM.
    //    1. Two threads writing to separate locations in memory do not need
    //      synchronization.
    //    2. A thread X that calls join on another thread Y is guaranteed to
    //      observe all the writes by thread Y after join returns.
  }
}