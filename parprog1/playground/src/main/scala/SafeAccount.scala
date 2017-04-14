
class SafeAccount(private var amount: Int = 0) {

  val uid = Main.getUniqueIdSafe

  private def lockAndTransfer(target: SafeAccount, n: Int): Unit = {
    this.synchronized {
      target.synchronized {
        this.amount -= n
        target.amount += n
      }
    }
  }

  def transfer(target: SafeAccount, n: Int): Unit = {
    if (this.uid < target.uid) this.lockAndTransfer(target, n)
    else target.lockAndTransfer(this, -n)
  }
}