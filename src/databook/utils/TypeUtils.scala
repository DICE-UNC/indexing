package databook.utils

import scala.reflect.runtime.universe._

object TypeUtils {
	def classOf[T:TypeTag]() = typeTag[T].mirror.runtimeClass(typeOf[T])
}
