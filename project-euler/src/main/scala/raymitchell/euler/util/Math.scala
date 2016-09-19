package raymitchell.euler.util

import scala.annotation.tailrec

object Math {
  import scala.language.implicitConversions

  implicit def long2FancyLong(x: Long): FancyLong = new FancyLong(x)

  /**
    * All Fibonacci numbers.
    *
    * Determine the next Fibonacci number by adding previous two numbers.
    */
  val fibonacciSequence: Stream[Int] = 0 #:: 1 #::
    fibonacciSequence
      .zip(fibonacciSequence.tail)  // Zip n-2 and n-1 together
      .map(n => n._1 + n._2)        // n-2 + n-1

  /**
    * All prime numbers.
    *
    * Determine the next prime by iterating through natural numbers and finding
    * the next number that has no previous prime as a factor.
    */
  val primeSequence: Stream[Int] = 2 #::
    Stream
      .from(3)
      .filter(n => primeSequence    // Possible factors = previous primes
        .takeWhile(p => p * p <= n) // Max possible factor is sqrt(n)
        .forall(n % _ != 0))        // To be prime must have no factors
}

final class FancyLong(self: Long) {
  import raymitchell.euler.util.Math._

  /**
    * Get the prime factors of a number.
    *
    * A prime number is a factor of itself.  If a prime factor appears more
    * than once it will be repeated in the returned list.
    *
    * Examples:  8.primeFactors  => List(2, 2, 2)
    *            17.primeFactors => List(17)
    */
  def primeFactors: Seq[Long] = {

    @tailrec
    def go(a: Long, primes: Seq[Int], factors: List[Long]): List[Long] = {
      val p = primes.head

      if (a == 1)               factors                         // All found
      else if (p * p > self)
           if (factors.isEmpty) List(self)                      // self prime
           else                 factors                         // No more
      else if (a % p == 0)      go(a / p, primes, p :: factors) // Found
      else                      go(a, primes.tail, factors)     // Next
    }

    go(self, primeSequence, Nil)
  }

  /**
    * Get greatest common divisor.
    *
    * Implemented using Euclid's algorithm.
    */
  @tailrec
  def gcd(other: Long): Long =
    if (other == 0) self
    else            other gcd (self % other)

  /**
    * Get least common multiple.
    *
    * Implemented using reduction by the greatest common divisor.
    */
  def lcm(other: Long): Long = self * (other / (self gcd other))
}