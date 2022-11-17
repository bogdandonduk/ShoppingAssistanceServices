package com.shoppingassistanceservices.data.model.appconfig.nativeapp

import com.shoppingassistanceservices.data.model.appconfig.AppConfigDetectionMethod

internal sealed class NativeAppConfigDetectionMethod: AppConfigDetectionMethod {
  class DetectByContent(val content: List<String>): NativeAppConfigDetectionMethod()
}
