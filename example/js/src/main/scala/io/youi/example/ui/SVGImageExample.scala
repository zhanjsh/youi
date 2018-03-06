//package io.youi.example.ui
//
//import io.youi._
//import io.youi.app.screen.UIScreen
//import io.youi.component.ImageView
//import io.youi.dom._
//import io.youi.image.{Image, SVGImage}
//import org.scalajs.dom.raw.SVGCircleElement
//
//import scala.concurrent.Future
//
//object SVGImageExample extends UIExampleScreen with UIScreen {
//  override def name: String = "SVG Image"
//  override def path: String = "/examples/svg-image.html"
//
//  private val svgString =
//    """
//      |<svg height="100" width="100">
//      |  <circle id="circle" cx="50" cy="50" r="40" stroke="black" stroke-width="3" fill="red" />
//      |</svg>
//    """.stripMargin
//
//  override def createUI(): Future[Unit] = for {
//    tiger <- Image("/images/tiger.svg")
//    circle <- SVGImage(svgString)
//  } yield {
//    container.children += new ImageView {
//      image := tiger
//      position.left := 10.0
//      position.top := 10.0
//    }
//
//    container.children += new ImageView {
//      image := circle
//      position.center := ui.center
//      position.middle := ui.middle
//
//      def toggleColor(): Unit = circle.modify { svg =>
//        val circle = svg.byId[SVGCircleElement]("circle")
//        if (circle.style.fill == "blue") {
//          circle.style.fill = "red"
//        } else {
//          circle.style.fill = "blue"
//        }
//      }
//
//      event.click.on(toggleColor())
//    }
//  }
//}