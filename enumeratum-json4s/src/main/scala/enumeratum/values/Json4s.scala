package enumeratum.values

import org.json4s.CustomSerializer
import org.json4s.JsonAST.{JInt, JLong, JString}

@SuppressWarnings(Array("org.wartremover.warts.Any"))
object Json4s {

  def serializer[A <: IntEnumEntry: Manifest](enum: IntEnum[A]): CustomSerializer[A] =
    new CustomSerializer[A](
      _ =>
        (
          {
            case JInt(i) if inBounds(i, Int.MaxValue) && enum.withValueOpt(i.toInt).isDefined =>
              enum.withValue(i.toInt)
            case JLong(i) if inBounds(i, Int.MaxValue) && enum.withValueOpt(i.toInt).isDefined =>
              enum.withValue(i.toInt)
          }, {
            case x: A => JLong(x.value.toLong)
          }
      ))

  def serializer[A <: LongEnumEntry: Manifest](enum: LongEnum[A]): CustomSerializer[A] =
    new CustomSerializer[A](
      _ =>
        (
          {
            case JInt(i) if isValidLong(i) && enum.withValueOpt(i.toLong).isDefined =>
              enum.withValue(i.toLong)
            case JLong(i) if enum.withValueOpt(i).isDefined => enum.withValue(i)
          }, {
            case x: A => JLong(x.value)
          }
      ))

  def serializer[A <: ShortEnumEntry: Manifest](enum: ShortEnum[A]): CustomSerializer[A] =
    new CustomSerializer[A](
      _ =>
        (
          {
            case JInt(i)
                if inBounds(i, Short.MaxValue) && enum.withValueOpt(i.toShort).isDefined =>
              enum.withValue(i.toShort)
            case JLong(i)
                if inBounds(i, Short.MaxValue) && enum.withValueOpt(i.toShort).isDefined =>
              enum.withValue(i.toShort)
          }, {
            case x: A => JLong(x.value.toLong)
          }
      ))

  def serializer[A <: StringEnumEntry: Manifest](enum: StringEnum[A]): CustomSerializer[A] =
    new CustomSerializer[A](
      _ =>
        (
          {
            case JString(s) if enum.withValueOpt(s).isDefined => enum.withValue(s)
          }, {
            case x: A => JString(x.value)
          }
      ))

  def serializer[A <: ByteEnumEntry: Manifest](enum: ByteEnum[A]): CustomSerializer[A] =
    new CustomSerializer[A](
      _ =>
        (
          {
            case JInt(i) if inBounds(i, Byte.MaxValue) && enum.withValueOpt(i.toByte).isDefined =>
              enum.withValue(i.toByte)
            case JLong(i) if inBounds(i, Byte.MaxValue) && enum.withValueOpt(i.toByte).isDefined =>
              enum.withValue(i.toByte)
          }, {
            case x: A => JLong(x.value.toLong)
          }
      ))

  def serializer[A <: CharEnumEntry: Manifest](enum: CharEnum[A]): CustomSerializer[A] =
    new CustomSerializer[A](
      _ =>
        (
          {
            case JString(s) if s.length == 1 && enum.withValueOpt(s.head).isDefined =>
              enum.withValue(s.head)
          }, {
            case x: A => JString(x.value.toString)
          }
      ))

  private def inBounds[N](o: N, posMax: Int)(implicit numeric: Numeric[N]): Boolean = {
    import numeric._
    abs(o) <= fromInt(posMax)
  }

  private def isValidLong(b: BigInt): Boolean = {
    b.abs <= BigInt(Long.MaxValue)
  }

}