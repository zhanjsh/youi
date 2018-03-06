package io.youi.hypertext

import io.youi.event.{HTMLEvents, Mouse}
import io.youi.hypertext.border.BorderStyle
import io.youi.spatial.BoundingBox
import io.youi.{Color, Key, _}
import org.scalajs.dom.raw.MouseEvent
import org.scalajs.dom.{html, window}
import reactify._

import scala.collection.immutable.ListSet

/**
  * Selection provides a rectangular selection feature for multi-selecting elements on the screen.
  *
  * @param root the root element to attach events to
  * @param elements function to get a list of all elements to check for selection
  * @param autoStyle if true defines the default styling of the outline (defaults to true)
  * @param adjustX offset adjustment to properly handle for horizontal scrolling
  * @param adjustY offset adjustment to properly handle for vertical scrolling
  * @tparam T the type of elements to select
  */
abstract class Selection[T](root: html.Element,
                            elements: => ListSet[T],
                            autoStyle: Boolean = true,
                            adjustX: => Double = window.pageXOffset,
                            adjustY: => Double = window.pageYOffset,
                            includeChildTargets: Boolean = false) extends Container {
  element.style.pointerEvents = "none"

  val enabled: Var[Boolean] = Var(true)
  val time: Var[Long] = Var(0L)
  val x1: Var[Double] = Var(0.0)
  val y1: Var[Double] = Var(0.0)
  val x2: Var[Double] = Var(0.0)
  val y2: Var[Double] = Var(0.0)
  val highlighted: Var[ListSet[T]] = Var(ListSet.empty)
  val selected: Var[ListSet[T]] = Var(ListSet.empty)

  // Default settings
  visible := false
  if (autoStyle) {
    border.style := Some(BorderStyle.Solid)
    border.size := Some(1.0)
    border.color := Some(Color.Black)
  }
  element.style.zIndex = "9999"

  position.x := math.min(x1, x2)
  position.y := math.min(y1, y2)
  size.width := math.abs(x1 - x2)
  size.height := math.abs(y1 - y2)

  val rootListener: SelectionListener[T] = new SelectionListener[T](this, root, deferToRoot = false, includeChildTargets = includeChildTargets)

  def addListener(base: html.Element,
                  deferToRoot: Boolean = true,
                  includeChildTargets: Boolean = false): SelectionListener[T] = {
    new SelectionListener[T](this, base, deferToRoot, includeChildTargets)
  }

  ui.event.key.down.attach { evt =>
    if (enabled()) {
      if (!visible()) {
        evt.key match {
          case Key.Shift | Key.Control => updateHighlighting(Mouse.x, Mouse.y, Mouse.x, Mouse.y)
          case _ => // Ignore others
        }
      }
    }
  }

  ui.event.key.up.attach { evt =>
    if (enabled()) {
      if (!visible()) {
        evt.key match {
          case Key.Shift | Key.Control => changeHighlighting(ListSet.empty)
          case _ => // Ignore others
        }
      }
    }
  }

  protected def updateHighlighting(x1: Double, y1: Double, x2: Double, y2: Double): Unit = {
    val highlighting = select(x1, y1, x2, y2, elements)
    changeHighlighting(highlighting)
  }

  protected def changeHighlighting(highlighting: ListSet[T]): Unit = {
    highlighted := highlighting
  }

  protected def selectHighlighted(toggle: Boolean): Unit = {
    val items = if (toggle) {
      val diff1 = selected().diff(highlighted())
      val diff2 = highlighted().diff(selected())
      diff1 ++ diff2
    } else {
      selected() ++ highlighted()
    }
    highlighted := ListSet.empty
    selected.static(items)
  }

  highlighted.changes(new ChangeObserver[ListSet[T]] {
    override def change(oldValue: ListSet[T], newValue: ListSet[T]): Unit = {
      val removed = oldValue -- newValue
      val added = newValue -- oldValue
      removed.foreach { e =>
        val isSelected = selected().contains(e)
        highlightRemoved(e, isSelected)
      }
      added.foreach(highlightAdded)
    }
  })

  selected.changes(new ChangeObserver[ListSet[T]] {
    override def change(oldValue: ListSet[T], newValue: ListSet[T]): Unit = {
      val removed = oldValue -- newValue
      val added = newValue -- oldValue
      removed.foreach(selectionRemoved)
      added.foreach(selectionAdded)
    }
  })

  def highlightAdded(element: T): Unit = {}
  def highlightRemoved(element: T, isSelected: Boolean): Unit = {}

  def selectionAdded(element: T): Unit = {}
  def selectionRemoved(element: T): Unit = {}

  def boxFor(element: T): BoundingBox

  def select(x1: Double, y1: Double, x2: Double, y2: Double, elements: ListSet[T]): ListSet[T] = {
    val minX = math.min(x1, x2) - adjustX
    val minY = math.min(y1, y2) - adjustY
    val maxX = math.max(x1, x2) - adjustX
    val maxY = math.max(y1, y2) - adjustY
    val boundingBox = BoundingBox(minX, minY, maxX, maxY)
    elements.filter { e =>
      val b = boxFor(e)
      boundingBox.intersects(b)
    }
  }

  private[hypertext] def down(evt: MouseEvent): Boolean = if (enabled()) {
    val touching = select(evt.pageX, evt.pageY, evt.pageX, evt.pageY, elements)
    if (touching.isEmpty) {
      evt.preventDefault()
      evt.stopPropagation()

      time := System.currentTimeMillis()
      x1 := evt.pageX
      y1 := evt.pageY
      x2 := evt.pageX
      y2 := evt.pageY
      visible := true
      true
    } else {
      false
    }
  } else {
    false
  }

  private[hypertext] def move(evt: MouseEvent): Unit = {
    if (enabled()) {
      if (visible()) {
        evt.preventDefault()
        evt.stopPropagation()

        x2 := evt.pageX
        y2 := evt.pageY

        updateHighlighting(x1, y1, x2, y2)
      } else if (evt.getModifierState(Key.Shift.value) || evt.getModifierState(Key.Control.value)) {
        updateHighlighting(evt.pageX, evt.pageY, evt.pageX, evt.pageY)
      }
    }
  }

  private[hypertext] def up(evt: MouseEvent): Unit = {
    if (enabled()) {
      if (visible()) {
        evt.preventDefault()
        evt.stopPropagation()

        selectHighlighted(evt.getModifierState(Key.Control.value))

        x1 := 0.0
        y1 := 0.0
        x2 := 0.0
        y2 := 0.0
        visible := false
      } else if (evt.getModifierState(Key.Control.value)) {
        updateHighlighting(evt.pageX, evt.pageY, evt.pageX, evt.pageY)
        selectHighlighted(toggle = true)
      } else if (evt.getModifierState(Key.Shift.value)) {
        select(evt.pageX, evt.pageY, evt.pageX, evt.pageY, elements).headOption.foreach { item =>
          selected().lastOption match {
            case Some(last) => {
              val items = elements.dropWhile(_ != last).takeWhile(_ != item) ++ ListSet(item)
              changeHighlighting(items)
              selectHighlighted(toggle = false)
            }
            case None => {
              changeHighlighting(ListSet(item))
              selectHighlighted(toggle = false)
            }
          }
        }
      }
    }
  }
}

class SelectionListener[T](selection: Selection[T], base: html.Element, deferToRoot: Boolean, includeChildTargets: Boolean) {
  var active: Boolean = false

  private val events = new HTMLEvents(base)

  events.pointer.down.attach { evt =>
    if (includeChildTargets || evt.target == base) {
      val active = selection.down(evt)
      if (deferToRoot && active) {
        selection.rootListener.active = active
      } else {
        this.active = active
      }
    }
  }

  events.pointer.move.attach { evt =>
    if (active) selection.move(evt)
  }

  events.pointer.up.attach { evt =>
    if (active) {
      selection.up(evt)
      active = false
    }
  }
}