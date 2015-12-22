package gen

import org.specs2.Specification
import shapeless.Sized

class MarkovSpec extends Specification { def is = s2"""

  Given an order, Markov should
    build a dictionary based on given list of tokens             $m1
    create random stream of tokens from given dictionary         $m2
    create random stream of tokens from given list of tokens     $m3

  """

  def m1 = markov.build(data) === dict
  def m2 = markov.gen(dict).run(seed).toList === data
  def m3 = markov.run(data).run(seed).toList === data

  val markov = Markov.Order2
  val seed   = 1L // Magic seed that creates "index 1"
  val data   = "This is my test data".split(" ").toList
  val dict   = Map(
    Sized[Vector]("This", "is") -> Vector("my"),
    Sized[Vector]("is", "my") -> Vector("test"),
    Sized[Vector]("my", "test") -> Vector("data"))

}
