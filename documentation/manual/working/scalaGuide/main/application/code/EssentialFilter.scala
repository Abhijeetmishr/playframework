/*
 * Copyright (C) from 2022 The Play Framework Contributors <https://github.com/playframework>, 2011-2021 Lightbend Inc. <https://www.lightbend.com>
 */

package scalaguide.advanced.filters.essential

// #essential-filter-example
import javax.inject.Inject
import akka.util.ByteString
import play.api.Logging
import play.api.libs.streams.Accumulator
import play.api.mvc._
import scala.concurrent.ExecutionContext

class LoggingFilter @Inject() (implicit ec: ExecutionContext) extends EssentialFilter with Logging {
  def apply(nextFilter: EssentialAction) = new EssentialAction {
    def apply(requestHeader: RequestHeader) = {
      val startTime = System.currentTimeMillis

      val accumulator: Accumulator[ByteString, Result] = nextFilter(requestHeader)

      accumulator.map { result =>
        val endTime     = System.currentTimeMillis
        val requestTime = endTime - startTime

        logger.info(
          s"${requestHeader.method} ${requestHeader.uri} took ${requestTime}ms and returned ${result.header.status}"
        )
        result.withHeaders("Request-Time" -> requestTime.toString)
      }
    }
  }
}
// #essential-filter-example
