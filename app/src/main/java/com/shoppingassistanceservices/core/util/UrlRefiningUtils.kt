package com.shoppingassistanceservices.core.util

object UrlRefiningUtils {
  fun trim(url: String): String =
    url.substringAfter("://")
      .substringAfter("www.")
      .trimEnd('/')
      .lowercase()

  fun trimToBase(url: String, raw: Boolean = true): String =
    if (raw) trim(url).substringBefore("/") else url.substringBefore("/")
}