package com.shoppingassistanceservices.data.model.appconfig.nativeapp

import com.shoppingassistanceservices.data.model.appconfig.AppConfig

internal data class NativeAppConfig(
  val appId: String,
  val detectionMethod: NativeAppConfigDetectionMethod
): AppConfig