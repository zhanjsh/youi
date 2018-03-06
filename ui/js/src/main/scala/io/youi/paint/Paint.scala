package io.youi.paint

import io.youi.net.URL
import io.youi.{Color, ImageMode, Modifiable, Updatable, ui}
import io.youi.drawable.{Context, Drawable}
import io.youi.image.Image
import io.youi.video.Video
import reactify.Var

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs._
import scala.scalajs.js.|

trait Paint extends Modifiable with Updatable {
  def isEmpty: Boolean = false
  def nonEmpty: Boolean = !isEmpty
  def update(delta: Double): Unit = {}
  def asJS(context: Context): js.Any
}

object Paint {
  def none: Paint = NoPaint
  def color(color: Color): Paint = ColorPaint(color)
  def horizontal(width: Double): LinearGradientPaint = linear(0.0, 0.0, width, 0.0)
  def vertical(height: Double): LinearGradientPaint = linear(0.0, 0.0, 0.0, height)
  def linear(x0: Double, y0: Double, x1: Double, y1: Double): LinearGradientPaint = {
    LinearGradientPaint(x0, y0, x1, y1)
  }
  def radial(x0: Double, y0: Double, r0: Double, x1: Double, y1: Double, r1: Double): RadialGradientPaint = {
    RadialGradientPaint(x0, y0, r0, x1, y1, r1)
  }
  def drawable[D <: Drawable](drawable: D,
                              width: => Double,
                              height: => Double,
                              x: => Double = 0.0,
                              y: => Double = 0.0,
                              ratio: => Double = ui.ratio,
                              repetition: Repetition = Repetition.Repeat): DrawablePaint[D] = new DrawablePaint[D](
    drawable = drawable,
    repetition = repetition,
    width = width,
    height = height,
    x = x,
    y = y,
    ratio = ratio
  )

  def image(url: String | URL,
            repetition: Repetition = Repetition.Repeat,
            mode: ImageMode = ImageMode.Quality,
            x: => Double = 0.0,
            y: => Double = 0.0,
            ratio: => Double = ui.ratio): Future[DrawablePaint[Image]] = Image(url.toString).map { image =>
    new DrawablePaint[Image](image, repetition, image.width, image.height, x, y, ratio)
  }

  def video(url: URL,
            repetition: Repetition = Repetition.Repeat,
            autoPlay: Boolean = true,
            loop: Boolean = true,
            muted: Boolean = true,
            x: => Double = 0.0,
            y: => Double = 0.0,
            ratio: => Double = ui.ratio): Future[DrawablePaint[Video]] = Video(url, autoPlay, loop, muted).map { video =>
    new DrawablePaint[Video](video, repetition, video.width, video.height, x, y, ratio)
  }
}