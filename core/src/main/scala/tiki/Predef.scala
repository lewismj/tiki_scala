/*
 * Copyright (c) 2017
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package tiki

/**
  * Predef is a set of type aliases for types which are commonly used in the library.
  * Mainly designed to ensure that algorithms within the library don't accidentally use
  * mutable collections or other parts of the `scala.Predef` that are not required.
  */
object Predef {

  def identity[A](x: A): A = x
  def implicitly[T](implicit e: T) = e

  type Boolean = scala.Boolean
  type Char = scala.Char
  type Unit = scala.Unit
  type Double = scala.Double
  type Long = scala.Long
  type Int = scala.Int
  type StringContext = scala.StringContext
  type Map[A,B] = scala.collection.immutable.Map[A,B]
  type Option[A]  = scala.Option[A]
  type Stream[A]  = scala.collection.immutable.Stream[A]
  type Iterable[A] = scala.collection.immutable.Iterable[A]
  type Traversable[A] = scala.collection.immutable.Traversable[A]
  type List[A] = scala.collection.immutable.List[A]
  type String = java.lang.String
  type tailrec = scala.annotation.tailrec
  type Set[A]     = scala.collection.immutable.Set[A]

  type Seq[A]     = scala.collection.immutable.Seq[A]
  /* `.toSeq` can return scala.collection.Seq, force to immutable. */
  implicit def seq[A](a: scala.collection.Seq[A]): Seq[A] = a.asInstanceOf[Seq[A]]

  /* Pattern matching sequences.*/
  object +: {
    def unapply[A](s: Seq[A]): Option[(A,Seq[A])] = if (s.nonEmpty) Some((s.head,s.tail)) else None
  }

  final val :: = scala.collection.immutable.::
  final val Nil = scala.collection.immutable.Nil
  final val Traversable = scala.collection.immutable.Traversable
  final val Iterable = scala.collection.immutable.Iterable
  final val Some = scala.Some
  final val None = scala.None
  final val Set = scala.collection.immutable.Set
  final val Seq = scala.collection.immutable.Seq
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

  final class ArrowAssoc[A](val x: A) {
    def -> [B](y: B): (A, B) = (x, y)
    def →[B](y: B): (A, B) = ->(y)
  }
  implicit def any2ArrowAssoc[A](x: A): ArrowAssoc[A] = new ArrowAssoc(x)

}
