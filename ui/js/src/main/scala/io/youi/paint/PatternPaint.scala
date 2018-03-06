package io.youi.paint

import io.youi.drawable.Context
import org.scalajs.dom.CanvasPattern

import scala.scalajs.js

trait PatternPaint extends Paint {
  def createPattern(): CanvasPattern

  override def asJS(context: Context): js.Any = if (context.canvas.width > 0 && context.canvas.height > 0) {
    createPattern()
  } else {
    ""
  }
}
