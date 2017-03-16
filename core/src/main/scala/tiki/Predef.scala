package tiki

/**
  * Predef is a set of type aliases for types which are commonly used in the library.
  * Mainly designed to ensure that algorithms within the library don't accidentally use
  * mutable collections or other parts of the `scala.Predef` that are not required.
  */
object Predef {

  def identity[A](a: A): A = a
  def implicitly[A](implicit a: A): A = a

  type Unit = scala.Unit
  type Double = scala.Double
  type Long = scala.Long
  type Int = scala.Int
  type StringContext = scala.StringContext
  type Map[A,B] = scala.collection.immutable.Map[A,B]
  type Set[A]     = scala.collection.immutable.Set[A]
  type Option[A]  = scala.Option[A]
  type Stream[A]  = scala.collection.immutable.Stream[A]
  type Iterable[A] = scala.collection.immutable.Iterable[A]
  type Traversable[A] = scala.collection.immutable.Traversable[A]
  type List[A] = scala.collection.immutable.List[A]
  type String = java.lang.String

  final val Traversable = scala.collection.immutable.Traversable
  final val Iterable = scala.collection.immutable.Iterable
  final val Some = scala.Some
  final val None = scala.None
  final val Set = scala.collection.immutable.Set
  final val Stream = scala.collection.immutable.Stream
  final val Map = scala.collection.immutable.Map
  final val List = scala.collection.immutable.List
  final val BigInt = scala.BigInt
  final val BigDecimal = scala.BigDecimal
  final val Boolean = scala.Boolean
  final val Byte = scala.Byte
  final val Char = scala.Char
  final val Double = scala.Double
  final val Float = scala.Float
  final val Int = scala.Int
  final val Long = scala.Long
  final val Short = scala.Short
  final val Unit = scala.Unit
  final val StringContext = scala.StringContext

}
