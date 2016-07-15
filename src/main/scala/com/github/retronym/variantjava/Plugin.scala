package com.github.retronym.variantjava

import scala.tools.nsc.{ Global, Phase }
import scala.reflect.internal.Variance
import scala.reflect.internal.Variance._

class Plugin(val global: Global) extends scala.tools.nsc.plugins.Plugin {
  import global._

  val name = "variant-java-plugin"
  val description = "Makes java.util.function._ appear to have declaration site variance"
  val components = Nil

  override def init(options: List[String], error: String => Unit): Boolean = {
    def setVariance(className: String, variances: Variance*) {
      val cls = rootMirror.getClassIfDefined(className)
      if (cls != NoSymbol) {
        if (cls.typeParams.length == variances.length) {
          (cls.typeParams, variances).zipped.foreach {
            (tparam, variance) =>
              val flag = variance match {
                case Variance.Covariant => Flag.COVARIANT
                case Variance.Contravariant => Flag.CONTRAVARIANT
                case _ => 0L
              }
              // println(s"making ${tparam.fullLocationString} $variance")
              tparam.setFlag(flag)
          }
        }
      }
    }    
    setVariance("java.util.function.BiFunction", Contravariant, Contravariant, Covariant)
    setVariance("java.util.function.Function", Contravariant, Covariant)
    setVariance("java.util.function.ObjLongConsumer", Contravariant)
    setVariance("java.util.function.BiConsumer", Contravariant, Contravariant)
    setVariance("java.util.function.Consumer", Contravariant)
    setVariance("java.util.function.ToIntFunction", Contravariant)
    setVariance("java.util.function.ObjIntConsumer", Contravariant)
    setVariance("java.util.function.ToIntBiFunction", Contravariant, Contravariant)
    setVariance("java.util.function.BiPredicate", Contravariant, Contravariant)
    setVariance("java.util.function.Predicate", Contravariant)
    setVariance("java.util.function.ToLongBiFunction", Contravariant, Contravariant)
    setVariance("java.util.function.DoubleFunction", Covariant)
    setVariance("java.util.function.ToLongFunction", Contravariant)
    setVariance("java.util.function.Supplier", Covariant)
    setVariance("java.util.function.ObjDoubleConsumer", Contravariant)
    setVariance("java.util.function.LongFunction", Covariant)
    setVariance("java.util.function.IntFunction", Covariant)
    setVariance("java.util.function.ToDoubleFunction", Contravariant)
    setVariance("java.util.function.ToDoubleBiFunction", Contravariant, Contravariant)
    true
  }
}
/*
scala> val j_u_f = rootMirror.getPackage("java.util.function")
warning: there was one deprecation warning (since 2.11.0); re-run with -deprecation for details
j_u_f: $r.intp.global.ModuleSymbol = package function

scala> val polyFunctions = j_u_f.info.members.filter(_.isClass).map(_.initialize).filterNot(_.isMonomorphicType).toList
polyFunctions: List[$r.intp.global.Symbol] = List(trait BiFunction, trait Function, trait ObjLongConsumer, trait BiConsumer, trait Consumer, trait ToIntFunction, trait ObjIntConsumer, trait ToIntBiFunction, trait BiPredicate, trait Predicate, trait UnaryOperator, trait BinaryOperator, trait ToLongBiFunction, trait DoubleFunction, trait ToLongFunction, trait Supplier, trait ObjDoubleConsumer, trait LongFunction, trait IntFunction, trait ToDoubleFunction, trait ToDoubleBiFunction)

scala> exitingTyper(println(polyFunctions.map(p => { val methType = samOf(p.tpe_*).initialize.info; "setVariance(\"" + p.fullName.toString + "\", " +  (p.typeParams map varianceInType(methType)).map(_.toString.capitalize).mkString(", ") + ")"}).mkString("\n")))
setVariance("java.util.function.BiFunction", Contravariant, Contravariant, Covariant)
setVariance("java.util.function.Function", Contravariant, Covariant)
setVariance("java.util.function.ObjLongConsumer", Contravariant)
setVariance("java.util.function.BiConsumer", Contravariant, Contravariant)
setVariance("java.util.function.Consumer", Contravariant)
setVariance("java.util.function.ToIntFunction", Contravariant)
setVariance("java.util.function.ObjIntConsumer", Contravariant)
setVariance("java.util.function.ToIntBiFunction", Contravariant, Contravariant)
setVariance("java.util.function.BiPredicate", Contravariant, Contravariant)
setVariance("java.util.function.Predicate", Contravariant)
setVariance("java.util.function.UnaryOperator", )
setVariance("java.util.function.BinaryOperator", )
setVariance("java.util.function.ToLongBiFunction", Contravariant, Contravariant)
setVariance("java.util.function.DoubleFunction", Covariant)
setVariance("java.util.function.ToLongFunction", Contravariant)
setVariance("java.util.function.Supplier", Covariant)
setVariance("java.util.function.ObjDoubleConsumer", Contravariant)
setVariance("java.util.function.LongFunction", Covariant)
setVariance("java.util.function.IntFunction", Covariant)
setVariance("java.util.function.ToDoubleFunction", Contravariant)
setVariance("java.util.function.ToDoubleBiFunction", Contravariant, Contravariant)
*/