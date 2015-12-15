package util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
  * Created by drashko on 29.11.15.
  */
object DateTimeUtil {

  val TRACKER_DATE_FORMAT: String = "ddMMyyHHmmssSS"
  val formatter = DateTimeFormatter.ofPattern(TRACKER_DATE_FORMAT);

  def getCurrentDateTimeAsString {
    LocalDate.now.format(formatter);
  }
}
