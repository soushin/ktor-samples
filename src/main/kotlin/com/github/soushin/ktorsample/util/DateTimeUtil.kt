package com.github.soushin.ktorsample.util

import org.threeten.bp.Clock
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeFormatterBuilder
import java.util.*

object DateTimeUtil {
    lateinit var systemClock: Clock
    lateinit var systemZoneId: ZoneId
    lateinit var systemLocale: Locale
    lateinit var dateRfc3339Formatter: DateTimeFormatter
    val ZONE_JST = ZoneOffset.ofHours(9)

    fun init(clock: Clock, locale: Locale = Locale.getDefault()) {
        systemClock = clock
        systemZoneId = clock.zone
        systemLocale = locale

        dateRfc3339Formatter = DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendLiteral('T')
            .append(DateTimeFormatter.ISO_LOCAL_TIME)
            .appendLiteral('Z')
            .toFormatter()
    }

    fun getCurrentZonedDateTime() = ZonedDateTime.now(systemClock)
}
