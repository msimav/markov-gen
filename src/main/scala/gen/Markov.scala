package gen

import shapeless._
import shapeless.ops.nat._
import shapeless.syntax.sized._

class Markov[N <: Nat] private (implicit n: ToInt[N]) {

  type State[T] = Markov.State[T, N]
  type Dict[T]  = Markov.Dict[T, N]

  val order = n()

  def build[T](xs: List[T]): Dict[T] = {
    val empty: Dict[T] =
      Map.empty.withDefaultValue(Vector.empty)

    def step(state: Dict[T], vector: Sized[Vector[T], Succ[N]]): Dict[T] = {
      val key = vector.init.ensureSized[N]
      state.updated(key, state(key) :+ vector.last)
    }

    xs.sliding(order + 1)
      .map(_.toVector.ensureSized[Succ[N]])
      .foldLeft(empty)(step)
  }

  def gen[T](dict: Dict[T]): Gen[Stream[T]] = Gen { random =>
    def rand[A](vector: Vector[A]): Option[A] =
      if (vector.isEmpty) None
      else vector.lift(random.nextInt(vector.size))

    def slide(state: State[T], next: T): State[T] =
      (state.unsized.tail :+ next).ensureSized[N]

    def loop(state: State[T]): Stream[T] =
      dict.get(state).flatMap(rand).fold(Stream.empty[T]) { next =>
        next #:: loop(slide(state, next))
      }

    val first = rand(dict.keys.toVector).get
    first.toStream append loop(first)
  }

  def run[T](xs: List[T]): Gen[Stream[T]] =
    gen(build(xs))

}

object Markov {

  type State[T, N <: Nat] = Sized[Vector[T], N]
  type Dict[T, N <: Nat]  = Map[State[T, N], Vector[T]]

  def apply[N <: Nat](implicit n: ToInt[N]): Markov[N] = new Markov[N]

  lazy val Order1 = Markov[Nat._1]
  lazy val Order2 = Markov[Nat._2]
  lazy val Order3 = Markov[Nat._3]
  lazy val Order4 = Markov[Nat._4]

}

