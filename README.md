## variant-java

A proof of concept Scala compiler plugin that makes functional interfaces
in the Java standard library more pleasant to work with from Scala by
treating them as though they had used declaration site variance.

[Compiler Plugin source](src/main/scala/com/github/retronym/variantjava/Plugin.scala)

As seen in the [demo](src/test/scala/demo/Demo.scala):

```scala
"abc".chars.boxed.map(x => x.toString).collect(Collectors.toList())
```

now typechecks without the need to explicitly provide the type argument to `map`.
