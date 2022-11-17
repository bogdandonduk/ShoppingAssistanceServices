package com.shoppingassistanceservices.data.model.appconfig.webapp

import com.shoppingassistanceservices.data.model.appconfig.AppConfig

internal data class WebAppConfig(
  val appId: String,
  val detectionMethod: WebAppConfigDetectionMethod
): AppConfig