//package io.youi.event
//
//import io.youi.component.Component
//import reactify.{Channel, Var}
//
//abstract class DragSupport[T](component: Component) {
//  val value: Var[Option[T]] = Var(None)
//  val drop: Channel[Dropped[T]] = Channel[Dropped[T]]
//
//  def isDragging: Boolean = value().nonEmpty
//
//  import component.event.gestures.pointers
//  pointers.added.attach { p =>
//    if (pointers.map.size > 1) {
//      value := None
//    } else {
//      checkForDown(p)
//    }
//  }
//  pointers.dragged.attach { p =>
//    value.foreach(v => dragging(p, v))
//  }
//  pointers.removed.attach(checkForUp)
//
//  /**
//    * Determines if there is draggable for this MouseEvent. This is called on mouse down events.
//    *
//    * @param pointer the event that triggered this draggable check
//    * @return optional T if there is a draggable for this mouse event
//    */
//  def draggable(pointer: Pointer): Option[T]
//
//  def dragging(pointer: Pointer, value: T): Unit = {}
//
//  def dropped(pointer: Pointer, value: T): Unit = {
//    drop := Dropped(pointer, value)
//  }
//
//  protected def checkForDown(pointer: Pointer): Unit = {
//    value.static(draggable(pointer))
//  }
//
//  protected def checkForUp(pointer: Pointer): Unit = value().foreach { v =>
//    dropped(pointer, v)
//    value := None
//  }
//}