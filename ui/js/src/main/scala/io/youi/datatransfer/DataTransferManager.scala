package io.youi.datatransfer

import io.youi.dom
import io.youi.dom._
import org.scalajs.dom._
import reactify._

import scala.language.implicitConversions

class DataTransferManager {
  implicit def file2Extras(file: File): FileExtras = file.asInstanceOf[FileExtras]

  val enabled: Var[Boolean] = Var(true)

  object overlay {
    val visible: Var[Boolean] = Var(false)
    val current: Var[Option[DragTarget]] = Var(None)

    def classOnVisible(target: html.Element,
                       className: String,
                       reverse: Boolean = false,
                       fireImmediately: Boolean = true): Unit = {
      def attachFunction(f: Boolean => Unit) = if (fireImmediately) visible.attachAndFire(f) else visible.attach(f)
      attachFunction { b =>
        val value = if (reverse) !b else b
        if (value) {
          target.classList.add(className)
        } else {
          target.classList.remove(className)
        }
      }
    }
  }
  val fileReceived: Channel[DataTransferFile] = Channel[DataTransferFile]
  val folderFeatureMissing: Channel[File] = Channel[File]

  def addDragTarget(element: html.Element): DragTarget = new HTMLDragTarget(element, this)

  def addInput(input: html.Input = dom.create[html.Input]("input"),
               modify: Boolean = true,
               folderSupport: Boolean = false): FileInput = new FileInput(input, modify, folderSupport, this)

  def process(files: FileList): Unit = files.toList.foreach { file =>
    if (file.`type`.isEmpty) {
      folderFeatureMissing := file
    } else {
      val fullName = try {
        file.webkitRelativePath match {
          case "" => file.name
          case path => path
        }
      } catch {
        case _: Throwable => file.name
      }
      val (path, fileName) = fullName match {
        case n if n.indexOf('/') != -1 => {
          val index = n.lastIndexOf('/')
          val pathString = n.substring(0, index)
          pathString.split('/').toList -> n.substring(index + 1)
        }
        case n => Nil -> n
      }
      fileReceived := DataTransferFile(file, fileName, path)
    }
  }
}