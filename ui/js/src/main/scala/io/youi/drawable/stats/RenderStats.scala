package io.youi.drawable.stats

import io.youi.drawable.{Context, Drawable}
import reactify._

class RenderStats {
  private val samples = 1000

  private var firstRender = 0L
  private var lastRender = 0L
  private var renderStart = 0L
  private var renderFinish = 0L
  private val count: Var[Long] = Var(0L)
  private var lastElapsed = 0.0

  private val elapsed: Array[Double] = new Array[Double](samples)
  private var position: Int = 0

  (0 until samples).foreach { index =>
    elapsed(index) = -1.0
  }

  def draw(drawable: Drawable, context: Context, x: Double, y: Double): Unit = {
    val start = System.nanoTime()
    drawable.draw(context, x, y)
    val finished = System.nanoTime()
    val elapsed = (finished - start) / 1000000000.0

    this.elapsed(position) = elapsed
    position += 1
    if (position == samples) {
      position = 0
    }

    if (firstRender == 0L) {
      firstRender = finished
    }
    lastRender = finished
    renderStart = start
    renderFinish = finished
    count.static(count + 1)
    lastElapsed = elapsed
  }

  def average: Double = {
    var count = 0.0
    var sum = 0.0
    elapsed.foreach { e =>
      if (e != -1.0) {
        sum += e
        count += 1.0
      }
    }
    sum / count
  }

  def min: Double = {
    elapsed.foldLeft(1000.0)((min, current) => if (current != -1.0) math.min(min, current) else min)
  }

  def max: Double = {
    elapsed.max match {
      case -1.0 => 0.0
      case d => d
    }
  }

  def renders: Val[Long] = count

  def current: Double = lastElapsed

  def fps: Int = math.round(1.0 / current).toInt

  def averageFPS: Int = math.round(1.0 / average).toInt

  override def toString: String = {
    f"Current: $fps fps ($current%2.2f), Average: $averageFPS ($average%2.2f), Min: $min%2.2f, Max: $max%2.2f, Renders: ${renders()}"
  }
}
