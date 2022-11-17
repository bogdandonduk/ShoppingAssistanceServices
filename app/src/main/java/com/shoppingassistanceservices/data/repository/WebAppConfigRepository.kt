package com.shoppingassistanceservices.data.repository

import com.shoppingassistanceservices.data.model.appconfig.webapp.WebAppConfig
import com.shoppingassistanceservices.data.model.appconfig.webapp.WebAppConfigDetectionMethod

internal object WebAppConfigRepository {
  val items = listOf(
    WebAppConfig(
      appId = "com.android.chrome",
      detectionMethod = WebAppConfigDetectionMethod.DetectByUrl("com.android.chrome:id/url_bar")
    ),
    WebAppConfig(
      appId = "org.mozilla.firefox",
      detectionMethod = WebAppConfigDetectionMethod.DetectByUrl("org.mozilla.firefox:id/mozac_browser_toolbar_url_view")
    ),
    WebAppConfig(
      appId = "com.opera.browser",
      detectionMethod = WebAppConfigDetectionMethod.DetectByUrl("com.opera.browser:id/url_field")
    ),
    WebAppConfig(
      appId = "com.huawei.hwsearch",
      detectionMethod = WebAppConfigDetectionMethod.DetectByUrl("com.huawei.hwsearch:id/webview_url")
    )
  )
}