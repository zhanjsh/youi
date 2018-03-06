package io.youi.event

import io.youi.Key

case class KeyEvent(`type`: KeyEvent.Type,
                    key: Key,
                    repeat: Boolean,
                    modifierState: Key => Boolean,
                    preventDefault: () => Unit,
                    defaultPrevented: () => Boolean,
                    stopImmediatePropagation: () => Unit,
                    stopPropagation: () => Unit) {
  def altPressed: Boolean = modifierState(Key.Alt)
  def altGraphPressed: Boolean = modifierState(Key.AltGraph)
  def controlPressed: Boolean = modifierState(Key.Control)
  def shiftPressed: Boolean = modifierState(Key.Shift)

  def capsLockOn: Boolean = modifierState(Key.CapsLock)
  def numLockOn: Boolean = modifierState(Key.NumLock)
  def scrollLockOn: Boolean = modifierState(Key.ScrollLock)

  override def toString: String = s"KeyEvent(key: $key, repeat: $repeat, alt: $altPressed, ctrl: $controlPressed, shift: $shiftPressed)"
}

object KeyEvent {
  sealed trait Type

  object Type {
    case object Down extends Type
    case object Press extends Type
    case object Up extends Type
  }
}