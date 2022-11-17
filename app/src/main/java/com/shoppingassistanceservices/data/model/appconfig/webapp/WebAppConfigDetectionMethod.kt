package com.shoppingassistanceservices.data.model.appconfig.webapp

import com.shoppingassistanceservices.data.model.appconfig.AppConfigDetectionMethod

internal sealed class WebAppConfigDetectionMethod: AppConfigDetectionMethod {
  class DetectByUrl(val urlWidgetId: String): WebAppConfigDetectionMethod()
  class DetectByContent(val content: List<String>): WebAppConfigDetectionMethod()
}
