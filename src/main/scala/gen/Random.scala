package gen

import util.{Random => SRandom}

sealed trait Random {
  def nextInt(n: Int): Int
}

sealed trait Gen[T] {
  def run(seed: Long): T
}

object Gen {

  def apply[T](block: Random => T): Gen[T] = new Gen[T] {
    def run(seed: Long): T =
      block(new Random {
        val rnd = new SRandom(seed)
        def nextInt(n: Int): Int = rnd.nextInt(n)
      })
  }

}
