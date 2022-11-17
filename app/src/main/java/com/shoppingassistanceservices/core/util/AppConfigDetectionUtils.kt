package com.shoppingassistanceservices.core.util

import com.shoppingassistanceservices.data.model.appconfig.AppConfig
import com.shoppingassistanceservices.data.repository.NativeAppConfigRepository
import com.shoppingassistanceservices.data.repository.WebAppConfigRepository

internal object AppConfigDetectionUtils {
  fun detectAppConfig(
    appId: CharSequence,
    webAppConfigRepository: WebAppConfigRepository,
    nativeAppConfigRepository: NativeAppConfigRepository,
  ): AppConfig? =
    webAppConfigRepository.items.find { it.appId == appId } ?: nativeAppConfigRepository.items.find { it.appId == appId }
}