package io.youi.server

import io.youi.http.{CacheControl, Content, HttpConnection, Status}
import io.youi.net.ContentType

object DefaultErrorHandler extends ErrorHandler {
  override def handle(connection: HttpConnection, t: Option[Throwable]): Unit = connection.update { response =>
    val status = if (response.status.isError) {
      response.status
    } else {
      Status.InternalServerError
    }
    val html =
      s"""<html>
         |  <head>
         |    <title>Error ${status.code}</title>
         |  </head>
         |  <body>
         |    ${status.code} - ${status.message}
         |  </body>
         |</html>
       """.stripMargin
    response
      .withContent(Content.string(html, ContentType.`text/html`))
      .withHeader(CacheControl(CacheControl.NoCache))
  }
}
