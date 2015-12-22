# markov-gen
Simple Markov Chain Text Generator in Scala

## Usage

```tut:silent
import gen.Markov
import scala.io.Source

object Example extends App {

  val markov = Markov.Order2
  val seed   = System.currentTimeMillis()
  val url    = getClass.getResource("/lorem-ipsum.txt")
  val words  = Source.fromURL(url).getLines().flatMap(_.split(" ")).toList
  val dict   = markov.build(words)
  val stream = markov.gen(dict).run(seed).take(300)

  println(stream.mkString(" "))

}
```
