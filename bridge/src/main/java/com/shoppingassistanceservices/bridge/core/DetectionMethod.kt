package com.shoppingassistanceservices.bridge.core

sealed class DetectionMethod {
  data class WebAppUrl(val url: String): DetectionMethod()

  data class NativeAppId(val appId: String): DetectionMethod()
}