package demo

import java.util.stream._

object Demo extends App {
  println("abc".chars.boxed.map(x => x.toString).collect(Collectors.toList()))
}
