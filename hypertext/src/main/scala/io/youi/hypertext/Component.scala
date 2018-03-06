package io.youi.hypertext

import io.youi._
import io.youi.event.HTMLEvents
import io.youi.hypertext.border.{Border, ComponentBorders}
import io.youi.hypertext.style.ComponentOverflow
import org.scalajs.dom._
import org.scalajs.dom.html.Element
import org.scalajs.dom.raw.Event
import reactify.Var

import scala.collection.mutable.ListBuffer

trait Component extends AbstractComponent {
  protected[youi] val element: Element

  val event: HTMLEvents = new HTMLEvents(element)

  lazy val border: ComponentBorders = new ComponentBorders(this)
  lazy val overflow: ComponentOverflow = new ComponentOverflow(this)
  lazy val outline: Border = new Border(this, element.style.outlineColor = _, element.style.outlineStyle = _, element.style.outlineWidth = _)

  def focus(): Unit = element.focus()
  def blur(): Unit = element.blur()
  def click(): Unit = element.click()

  protected[hypertext] def prop[T](get: => T, set: T => Unit, mayCauseResize: Boolean): Var[T] = {
    val v = Var[T](get)
    v.attach { value =>
      set(value)
      if (mayCauseResize) {
        nextFrame(updateSize())
      }
    }
    v
  }

  override protected def init(): Unit = {
    super.init()

    opacity := (try {
      element.style.opacity.toDouble
    } catch {
      case t: Throwable => 1.0
    })
    visible := element.style.visibility != "hidden"

    position.`type`.attachAndFire(p => element.style.position = p.toString.toLowerCase)
    position.x.attach(d => element.style.left = s"${d}px")
    position.y.attach(d => element.style.top = s"${d}px")
    size.width.attach { d =>
      element.style.width = s"${d}px"
      nextFrame(updateSize())
    }
    size.height.attach { d =>
      element.style.height = s"${d}px"
      nextFrame(updateSize())
    }
    size.min.width.attach { d =>
      element.style.minWidth = s"${d}px"
      nextFrame(updateSize())
    }
    size.min.height.attach { d =>
      element.style.minHeight = s"${d}px"
      nextFrame(updateSize())
    }
    size.max.width.attach { d =>
      element.style.maxWidth = s"${d}px"
      nextFrame(updateSize())
    }
    size.max.height.attach { d =>
      element.style.maxHeight = s"${d}px"
      nextFrame(updateSize())
    }
    parent.attach { p =>
      nextFrame(updateSize())
    }
    rotation.attach(d => updateTransform())
    scale.x.attach(d => updateTransform())
    scale.y.attach(d => updateTransform())
    opacity.attach(d => element.style.opacity = d.toString)
    visible.attach(b => element.style.visibility = if (b) "visible" else "hidden")
    color.red.attach(d => updateColor())
    color.green.attach(d => updateColor())
    color.blue.attach(d => updateColor())
    color.alpha.attach(d => updateColor())
    backgroundColor.red.attach(d => updateBackgroundColor())
    backgroundColor.green.attach(d => updateBackgroundColor())
    backgroundColor.blue.attach(d => updateBackgroundColor())
    backgroundColor.alpha.attach(d => updateBackgroundColor())
    padding.left.attach(d => element.style.paddingLeft = s"${d}px")
    padding.right.attach(d => element.style.paddingRight = s"${d}px")
    padding.top.attach(d => element.style.paddingTop = s"${d}px")
    padding.bottom.attach(d => element.style.paddingBottom = s"${d}px")
    margin.left.attach(d => element.style.marginLeft = s"${d}px")
    margin.right.attach(d => element.style.marginRight = s"${d}px")
    margin.top.attach(d => element.style.marginTop = s"${d}px")
    margin.bottom.attach(d => element.style.marginBottom = s"${d}px")

    element.addEventListener("scroll", (evt: Event) => {
      updateSize()
    })
    scrollbar.vertical.position.attach { p =>
      if (!updatingSize) {
        element.scrollTop = p
        updateSize()
      }
    }
    scrollbar.horizontal.position.attach { p =>
      if (!updatingSize) {
        element.scrollLeft = p
        updateSize()
      }
    }
    scrollbar.vertical.percentage.attach { p =>
      if (!updatingSize) {
        element.scrollTop = (size.inner.height() - size.actual.height()) * p
        updateSize()
      }
    }
    scrollbar.horizontal.percentage.attach { p =>
      if (!updatingSize) {
        element.scrollLeft = (size.inner.width() - size.actual.width()) * p
        updateSize()
      }
    }

    if (!color.isDefault) updateColor()
    if (!backgroundColor.isDefault) updateBackgroundColor()
    updateSize()
  }

  private var updatingSize: Boolean = false

  protected def determineActualWidth: Double = element.offsetWidth + margin.left() + margin.right()
  protected def determineActualHeight: Double = element.offsetHeight + margin.top() + margin.bottom()

  def updateSize(): Unit = if (!updatingSize) {
    updatingSize = true
    try {
      if (actualWidth() != determineActualWidth) actualWidth.static(determineActualWidth)
      if (actualHeight() != determineActualHeight) actualHeight.static(determineActualHeight)

      val h = scrollbar.horizontal.size.asInstanceOf[Var[Double]]
      val v = scrollbar.vertical.size.asInstanceOf[Var[Double]]

      h.set(math.max(0.0, element.offsetHeight - element.clientHeight - border.top.size().getOrElse(0.0) - border.bottom.size().getOrElse(0.0)))
      v.set(math.max(0.0, element.offsetWidth - element.clientWidth - border.left.size().getOrElse(0.0) - border.right.size().getOrElse(0.0)))

      scrollbar.horizontal.position := element.scrollLeft
      scrollbar.vertical.position := element.scrollTop
      innerWidth := element.scrollWidth
      innerHeight := element.scrollHeight

      scrollbar.vertical.percentage := scrollbar.vertical.position / (size.inner.height() - size.actual.height())
      scrollbar.horizontal.percentage := scrollbar.horizontal.position / (size.inner.width - size.actual.height)
    } finally {
      updatingSize = false
    }
  }

  protected def updateTransform(): Unit = {
    val b = ListBuffer.empty[String]
    if (rotation() != 0.0) {
      b += s"rotate(${rotation() * 360.0}deg)"
    }
    if (scale.x() != 1.0) {
      b += s"scaleX(${scale.x()})"
    }
    if (scale.y() != 1.0) {
      b += s"scaleY(${scale.y()})"
    }
    element.style.transform = b.mkString(" ")
  }

  protected def updateColor(): Unit = {
    element.style.color = Color.fromRGBA(color.red(), color.green(), color.blue(), color.alpha()).toRGBA
  }

  protected def updateBackgroundColor(): Unit = {
    val css = Color.fromRGBA(backgroundColor.red(), backgroundColor.green(), backgroundColor.blue(), backgroundColor.alpha()).toRGBA
    element.style.backgroundColor = css
  }
}

object Component {
  private var cache = Map.empty[html.Element, Component]

  def cached[E <: html.Element, T <: Component](element: E, create: E => T): T = synchronized {
    cache.get(element) match {
      case Some(c) => c.asInstanceOf[T]
      case None => {
        val c = create(element)
        AnimationFrame.delta.attach(c.update)     // Disconnected from a hierarchical parent, so it needs to update directly
        cache += element -> c
        c
      }
    }
  }
}