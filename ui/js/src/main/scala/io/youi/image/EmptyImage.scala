package io.youi.image

import io.youi.drawable.Context
import io.youi.image.resize.ImageResizer
import org.scalajs.dom.html

import scala.concurrent.Future

object EmptyImage extends Image {
  override val width: Double = 0.0
  override val height: Double = 0.0

  override def draw(context: Context, x: Double, y: Double, width: Double, height: Double): Unit = ()

  override def dispose(): Unit = {}

  override def isVector: Boolean = true

  override def toDataURL: Future[String] = throw new RuntimeException("Empty image cannot be represented as a data url.")

  override def resize(width: Double, height: Double): Future[Image] = Future.successful(this)

  override def resizeTo(canvas: html.Canvas, width: Double, height: Double, resizer: ImageResizer): Future[html.Canvas] = Future.successful(canvas)

  override def toString: String = "EmptyImage"
}