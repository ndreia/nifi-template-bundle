package com.tgsight

package object nifi {

  def withThrowableAsEither[A, R](a: A)(f: A => R): Either[Throwable, R] = {
    try {
      Right(f(a))
    } catch {
      case e: Throwable => Left(e)
    }
  }

  def withResource[A <: AutoCloseable, R](a: A)(f: A => R): R = {
    try {
      f(a)
    } finally {
      a.close()
    }
  }

  def assertNotNull[A <: AnyRef](a: A): A = {
    assert(a != null, "value was null")
    a
  }
}
