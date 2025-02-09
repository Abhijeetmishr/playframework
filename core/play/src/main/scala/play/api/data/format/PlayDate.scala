/*
 * Copyright (C) from 2022 The Play Framework Contributors <https://github.com/playframework>, 2011-2021 Lightbend Inc. <https://www.lightbend.com>
 */

package play.api.data.format

import java.time.format.DateTimeFormatter
import java.time.temporal._
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.ZoneOffset

private[play] object PlayDate {
  def parse(text: CharSequence, formatter: DateTimeFormatter): PlayDate = new PlayDate(formatter.parse(text))
}

private[play] class PlayDate(accessor: TemporalAccessor) {
  private[this] def getOrDefault(field: TemporalField, default: Int): Int = {
    if (accessor.isSupported(field)) accessor.get(field) else default
  }

  def toZonedDateTime(zoneId: ZoneId): ZonedDateTime = {
    val year: Int          = getOrDefault(ChronoField.YEAR, 1970)
    val month: Int         = getOrDefault(ChronoField.MONTH_OF_YEAR, 1)
    val day: Int           = getOrDefault(ChronoField.DAY_OF_MONTH, 1)
    val hour: Int          = getOrDefault(ChronoField.HOUR_OF_DAY, 0)
    val minute: Int        = getOrDefault(ChronoField.MINUTE_OF_HOUR, 0)
    val second: Int        = getOrDefault(ChronoField.SECOND_OF_MINUTE, 0)
    val nano: Int          = getOrDefault(ChronoField.NANO_OF_SECOND, 0)
    val offset: ZoneOffset = ZoneOffset.ofTotalSeconds(getOrDefault(ChronoField.OFFSET_SECONDS, 0))

    ZonedDateTime.ofInstant(LocalDateTime.of(year, month, day, hour, minute, second, nano), offset, zoneId)
  }
}
