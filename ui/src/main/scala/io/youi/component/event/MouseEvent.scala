package io.youi.component.event

import com.outr.pixijs.PIXI
import io.youi.Point
import io.youi.component.Component

class MouseEvent(val component: Component, val evt: PIXI.interaction.InteractionEvent) {
  lazy val globalX: Double = evt.data.global.x
  lazy val globalY: Double = evt.data.global.y
  lazy val (x, y) = {
    val p = evt.data.getLocalPosition(component.instance)
    p.x -> p.y
  }
  lazy val local: Point = Point(x, y)
  lazy val global: Point = Point(globalX, globalY)
  def stopped: Boolean = evt.stopped
  def stopPropagation(): Unit = evt.stopPropagation()
}