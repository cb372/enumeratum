package enumeratum

import scala.language.experimental.macros
import scala.language.postfixOps

/**
 * All the cool kids have their own Enumeration implementation, most of which try to
 * do so in the name of implementing exhaustive pattern matching.
 *
 * This is yet another one.
 *
 * Oh yeah, [[Enum]] is BYOO (bring your own ordinality). Take care of that when
 * you implement the value method.
 *
 * How to use:
 *
 * {{{
 * sealed trait DummyEnum
 *
 * object DummyEnum extends Enum[DummyEnum] {
 *
 * val values = findValues.toIndexedSeq
 *
 * case object Hello extends DummyEnum
 * case object GoodBye extends DummyEnum
 * case object Hi extends DummyEnum
 *
 * }
 *
 *
 * DummyEnum.values should be(Set(Hello, GoodBye, Hi))
 *
 * DummyEnum.withName("Hello") should be(Hello)
 * }}}
 * @tparam A The sealed trait you want to use as the main
 */
trait Enum[A] {

  /**
   * The sequence of values for your [[Enum]]. You will typically want
   * to implement this in your extending class as a `val` so that `withName`
   * and friends are as efficient as possible.
   *
   * Feel free to implement this however you'd like (including ordering, etc) if that
   * fits your needs better.
   */
  def values: Iterable[A]

  /**
   * Method that returns the Set of [[A]] objects that the macro was able to find.
   *
   * You will want to use this in some way to implement your [[values]] method. In fact,
   * if you aren't using this method...why are you even bothering with this lib?
   */
  protected def findValues: Set[A] = macro EnumMacros.findValuesImpl[A]

  /**
   * Map of [[A]] object names to [[A]]s
   */
  lazy final val namesToValuesMap: Map[String, A] = values map (v => v.toString -> v) toMap

  /**
   * Tries to get an [[A]] by the supplied name. The name corresponds to the .toString
   * of the case objects implementing [[A]]
   */
  def withName(name: String): A =
    namesToValuesMap getOrElse (name, throw new IllegalArgumentException(s"$name is not a member of Enum $this"))

}