package io.youi.spatial

import io.youi.spatial.ops.{Matrix3Addition, Matrix3Multiply, Matrix3Subtraction}

case class MutableMatrix3(var m00: Double,
                          var m01: Double,
                          var m02: Double,
                          var m10: Double,
                          var m11: Double,
                          var m12: Double,
                          var m20: Double,
                          var m21: Double,
                          var m22: Double) extends Matrix3 {
  override def isMutable: Boolean = true

  def set(m00: Double = m00,
             m01: Double = m01,
             m02: Double = m02,
             m10: Double = m10,
             m11: Double = m11,
             m12: Double = m12,
             m20: Double = m20,
             m21: Double = m21,
             m22: Double = m22): Matrix3 = {
    this.m00 = m00
    this.m01 = m01
    this.m02 = m02
    this.m10 = m10
    this.m11 = m11
    this.m12 = m12
    this.m20 = m20
    this.m21 = m21
    this.m22 = m22
    this
  }

  override def duplicate(m00: Double = m00,
                         m01: Double = m01,
                         m02: Double = m02,
                         m10: Double = m10,
                         m11: Double = m11,
                         m12: Double = m12,
                         m20: Double = m20,
                         m21: Double = m21,
                         m22: Double = m22): Matrix3 = copy(m00, m01, m02, m10, m11, m12, m20, m21, m22)

  def *=(that: Matrix3): Matrix3 = Matrix3Multiply(this, that, set)
  def +=(that: Matrix3): Matrix3 = Matrix3Addition(this, that, set)
  def -=(that: Matrix3): Matrix3 = Matrix3Subtraction(this, that, set)
  def *=(scalar: Double): Matrix3 = withArray(duplicate = false)(_.transform(_ * scalar))
  def +=(scalar: Double): Matrix3 = withArray(duplicate = false)(_.transform(_ + scalar))
  def -=(scalar: Double): Matrix3 = withArray(duplicate = false)(_.transform(_ - scalar))
  def /=(scalar: Double): Matrix3 = withArray(duplicate = false)(_.transform(_ / scalar))

  override def mutable: MutableMatrix3 = this
}