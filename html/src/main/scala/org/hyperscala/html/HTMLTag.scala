package org.hyperscala.html

import attributes._
import event.EventSupport
import org.hyperscala._
import org.hyperscala.html.tag._
import css.StyleSheet
import persistence.StyleSheetPersistence
import scala.collection.{Map => ScalaMap}
import org.powerscala.property.PropertyParent
import org.jdom2.Element

/**
 * NOTE: This file has been generated. Do not modify directly!
 * @author Matt Hicks <mhicks@hyperscala.org>
 */
trait HTMLTag extends Tag with EventSupport {
  val accessKey = PropertyAttribute[Char]("accesskey", -1.toChar)
  val clazz = PropertyAttribute[List[String]]("class", Nil)
  val contentEditable = PropertyAttribute[ContentEditable]("contenteditable", null)
  val contextMenu = PropertyAttribute[String]("contextmenu", null)
  val dir = PropertyAttribute[Direction]("dir", null)
  val draggable = PropertyAttribute[Draggable]("draggable", null)
  val dropZone = PropertyAttribute[DropZone]("dropzone", null)
  val hidden = PropertyAttribute[Boolean]("hidden", false)
  val id = PropertyAttribute[String]("id", null)
  val lang = PropertyAttribute[String]("lang", null)
  val spellCheck = PropertyAttribute[Boolean]("spellcheck", false)
  val tabIndex = PropertyAttribute[Int]("tabindex", -1)
  val title = PropertyAttribute[String]("title", null)

  val style = new StyleProperty("style", InclusionMode.NotEmpty) {
    val link = new StyleProperty("link", InclusionMode.Exclude)(HTMLTag.this)
    val visited = new StyleProperty("visited", InclusionMode.Exclude)(HTMLTag.this)
    val active = new StyleProperty("active", InclusionMode.Exclude)(HTMLTag.this)
    val hover = new StyleProperty("hover", InclusionMode.Exclude)(HTMLTag.this)
    val focus = new StyleProperty("focus", InclusionMode.Exclude)(HTMLTag.this)
    val empty = new StyleProperty("empty", InclusionMode.Exclude)(HTMLTag.this)
    val target = new StyleProperty("target", InclusionMode.Exclude)(HTMLTag.this)
    val checked = new StyleProperty("checked", InclusionMode.Exclude)(HTMLTag.this)
    val enabled = new StyleProperty("enabled", InclusionMode.Exclude)(HTMLTag.this)
    val disabled = new StyleProperty("disabled", InclusionMode.Exclude)(HTMLTag.this)
  }

  if (HTMLTag.GenerateIds) {
    id := Unique()
  }

  protected def generateChildFromTagName(name: String): XMLContent = {
    HTMLTag.create(name)
  }

  protected def processText(text: String): Unit = {
    this.asInstanceOf[Container[HTMLTag]].contents += new Text(text)
  }

  override protected def after() {
    super.after()

    // TODO: add support back for extra stylization
//    val extraStyle = new StringBuilder
//    validateExtraStyle(element, style.link, extraStyle)
//    validateExtraStyle(element, style.visited, extraStyle)
//    validateExtraStyle(element, style.active, extraStyle)
//    validateExtraStyle(element, style.hover, extraStyle)
//    validateExtraStyle(element, style.focus, extraStyle)
//    validateExtraStyle(element, style.empty, extraStyle)
//    validateExtraStyle(element, style.target, extraStyle)
//    validateExtraStyle(element, style.checked, extraStyle)
//    validateExtraStyle(element, style.enabled, extraStyle)
//    validateExtraStyle(element, style.disabled, extraStyle)
//    if (extraStyle.length != 0) {
//      val general = element.getAttributeValue("style") match {
//        case null => ""
//        case v => v
//      }
//      element.removeAttribute("style")
//      extraStyle.append("\t#%s { %s }\n".format(id(), general))
//
//      val styleTag = new Element("style")
//      styleTag.setText("\n%s".format(extraStyle.toString()))
//      element.addContent(styleTag)
//    }
  }

  private def validateExtraStyle(element: Element, s: StyleProperty, extraStyle: StringBuilder) = {
    if (s.loaded && s.modified) {
      val css = s.attributeValue
      if (css.nonEmpty) {
        if (id() == null) {
          id := Unique()
          element.setAttribute("id", id())
        }
        extraStyle.append("\t#%s:%s { %s }\n".format(id(), s.name(), css))
      }
    }
  }

  def byId[T <: HTMLTag](id: String)(implicit manifest: Manifest[T]) = hierarchy.findFirst[T](t => t.id() == id)(manifest)

  def byName[T <: HTMLTag](name: String)(implicit manifest: Manifest[T]) = hierarchy.findAll[T](t => t.name() == name)(manifest)

  def byTag[T <: HTMLTag](implicit manifest: Manifest[T]) = hierarchy.findAll[T](t => true)(manifest)
}

class StyleProperty(_name: String, inclusion: InclusionMode)(implicit parent: PropertyParent) extends PropertyAttribute[StyleSheet](_name, null, inclusion = inclusion)(StyleSheetPersistence, parent, Manifest.classType[StyleSheet](classOf[StyleSheet])) with LazyProperty[StyleSheet] {
  protected def lazyValue = new StyleSheet

  // Avoid overwriting previously set values
  override def attributeValue_=(value: String) = StyleSheetPersistence(this.value, value)
}

object HTMLTag {
  var GenerateIds = false

  private val registry = ScalaMap("html" -> classOf[HTML],
                                  "head" -> classOf[Head],
                                  "title" -> classOf[Title],
                                  "body" -> classOf[Body],
                                  "a" -> classOf[A],
                                  "abbr" -> classOf[Abbr],
                                  "address" -> classOf[Address],
                                  "area" -> classOf[Area],
                                  "article" -> classOf[Article],
                                  "aside" -> classOf[Aside],
                                  "audio" -> classOf[Audio],
                                  "b" -> classOf[B],
                                  "base" -> classOf[Base],
                                  "bdi" -> classOf[Bdi],
                                  "bdo" -> classOf[Bdo],
                                  "blockquote" -> classOf[BlockQuote],
                                  "br" -> classOf[Br],
                                  "button" -> classOf[Button],
                                  "canvas" -> classOf[Canvas],
                                  "caption" -> classOf[Caption],
                                  "cite" -> classOf[Cite],
                                  "code" -> classOf[Code],
                                  "col" -> classOf[Col],
                                  "colgroup" -> classOf[ColGroup],
                                  "command" -> classOf[Command],
                                  "datalist" -> classOf[DataList],
                                  "dd" -> classOf[Dd],
                                  "del" -> classOf[Del],
                                  "details" -> classOf[Details],
                                  "dfn" -> classOf[Dfn],
                                  "div" -> classOf[Div],
                                  "dl" -> classOf[Dl],
                                  "dt" -> classOf[Dt],
                                  "em" -> classOf[Em],
                                  "embed" -> classOf[Embed],
                                  "fieldset" -> classOf[FieldSet],
                                  "figcaption" -> classOf[FigCaption],
                                  "figure" -> classOf[Figure],
                                  "footer" -> classOf[Footer],
                                  "form" -> classOf[Form],
                                  "h1" -> classOf[H1],
                                  "h2" -> classOf[H2],
                                  "h3" -> classOf[H3],
                                  "h4" -> classOf[H4],
                                  "h5" -> classOf[H5],
                                  "h6" -> classOf[H6],
                                  "header" -> classOf[Header],
                                  "hgroup" -> classOf[HGroup],
                                  "hr" -> classOf[Hr],
                                  "i" -> classOf[I],
                                  "iframe" -> classOf[IFrame],
                                  "img" -> classOf[Img],
                                  "input" -> classOf[Input],
                                  "ins" -> classOf[Ins],
                                  "kbd" -> classOf[Kbd],
                                  "keygen" -> classOf[KeyGen],
                                  "label" -> classOf[Label],
                                  "legend" -> classOf[Legend],
                                  "li" -> classOf[Li],
                                  "link" -> classOf[Link],
                                  "map" -> classOf[tag.Map],
                                  "mark" -> classOf[Mark],
                                  "menu" -> classOf[Menu],
                                  "meta" -> classOf[Meta],
                                  "meter" -> classOf[Meter],
                                  "nav" -> classOf[Nav],
                                  "noscript" -> classOf[NoScript],
                                  "object" -> classOf[tag.Object],
                                  "ol" -> classOf[Ol],
                                  "optgroup" -> classOf[OptGroup],
                                  "option" -> classOf[tag.Option],
                                  "output" -> classOf[Output],
                                  "p" -> classOf[P],
                                  "param" -> classOf[Param],
                                  "pre" -> classOf[Pre],
                                  "progress" -> classOf[Progress],
                                  "q" -> classOf[Q],
                                  "rp" -> classOf[Rp],
                                  "rt" -> classOf[Rt],
                                  "ruby" -> classOf[Ruby],
                                  "s" -> classOf[S],
                                  "samp" -> classOf[Samp],
                                  "script" -> classOf[Script],
                                  "section" -> classOf[Section],
                                  "select" -> classOf[Select],
                                  "small" -> classOf[Small],
                                  "source" -> classOf[Source],
                                  "span" -> classOf[Span],
                                  "strong" -> classOf[Strong],
                                  "style" -> classOf[Style],
                                  "sub" -> classOf[Sub],
                                  "sup" -> classOf[Sup],
                                  "table" -> classOf[Table],
                                  "tbody" -> classOf[TBody],
                                  "td" -> classOf[Td],
                                  "textarea" -> classOf[TextArea],
                                  "tfoot" -> classOf[TFoot],
                                  "th" -> classOf[Th],
                                  "thead" -> classOf[THead],
                                  "time" -> classOf[Time],
                                  "tr" -> classOf[Tr],
                                  "track" -> classOf[Track],
                                  "u" -> classOf[U],
                                  "ul" -> classOf[Ul],
                                  "var" -> classOf[Var],
                                  "video" -> classOf[Video],
                                  "wbr" -> classOf[Wbr])

  def create(tagName: String) = {
    registry(tagName).newInstance().asInstanceOf[HTMLTag]
  }
}