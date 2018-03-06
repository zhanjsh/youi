//package io.youi.example.ui
//
//import io.youi._
//import io.youi.app.screen.UIScreen
//import io.youi.component.ImageView
//import io.youi.image.Image
//
//import scala.concurrent.Future
//
//object ImageChangeExample extends UIExampleScreen with UIScreen {
//  override def name: String = "Image Change"
//  override def path: String = "/examples/image-change.html"
//
//  lazy val imageView: ImageView = new ImageView
//
//  override def createUI(): Future[Unit] = for {
//    icon <- Image("/images/icon.png")
//    cuteness <- Image("/images/cuteness.jpg")
//    tiger <- Image("/images/tiger.svg")
//  } yield {
//    imageView.autoDispose := false
//    imageView.image := tiger
//    imageView.position.center := ui.center
//    imageView.position.middle := ui.middle
//
//    imageView.event.click.on {
//      val img = imageView.image()
//      if (img == icon) {
//        scribe.info("Icon -> Cuteness")
//        imageView.image := cuteness
//      } else if (img == cuteness) {
//        scribe.info("Cuteness -> Tiger")
//        imageView.image := tiger
//      } else if (img == tiger) {
//        scribe.info("Tiger -> Icon")
//        imageView.image := icon
//      } else {
//        scribe.error("No match!")
//      }
//    }
//
//    container.children += imageView
//  }
//}