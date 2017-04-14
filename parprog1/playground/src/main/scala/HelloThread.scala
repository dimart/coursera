class HelloThread extends Thread {
  override def run(): Unit = {
//    var a = 0
//    for (i <- 1 to 10000) {
//      a += 1
//    }
    println("Hello")
    println("Thread!")
  }
}
