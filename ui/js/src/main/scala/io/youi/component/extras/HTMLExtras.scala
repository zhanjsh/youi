package io.youi.component.extras

import org.scalajs.dom._
import org.scalajs.dom.raw.CSSStyleDeclaration

class HTMLExtras[E <: html.Element](val element: E) {
  private def style: CSSStyleDeclaration = window.getComputedStyle(element)
}
