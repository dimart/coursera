
class Account(private var amount: Int = 0) {
  def transfer(account: Account, n: Int): Unit = {
    this.synchronized { // instead of having a global sync object,
      account.synchronized { // we compose several sync blocks! (although it may lead to deadlocks)
        this.amount -= n
        account.amount += n
      }
    }
  }
}
