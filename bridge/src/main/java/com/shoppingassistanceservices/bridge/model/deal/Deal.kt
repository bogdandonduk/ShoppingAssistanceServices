package com.shoppingassistanceservices.bridge.model.deal

import com.shoppingassistanceservices.bridge.core.DetectionMethod

sealed class Deal(
  open val activationTokens: List<String>,
  open val webAppUrlDetectionMethods: List<DetectionMethod.WebAppUrl>,
  open val nativeAppIdDetectionMethods: List<DetectionMethod.NativeAppId>,
) {

  sealed class Info(open val storeName: String, open val description: String)

  data class Coupon(
    val promoCodes: List<String> = emptyList(),
    override val webAppUrlDetectionMethods: List<DetectionMethod.WebAppUrl> = emptyList(),
    override val nativeAppIdDetectionMethods: List<DetectionMethod.NativeAppId> = emptyList()
  ): Deal(promoCodes, webAppUrlDetectionMethods, nativeAppIdDetectionMethods) {
    data class Info(override val storeName: String, override val description: String): Deal.Info(storeName, description)

    data class RemoteInfo(fetchUri)
  }

  data class Cashback(
    val activationUrls: List<String> = emptyList(),
    override val webAppUrlDetectionMethods: List<DetectionMethod.WebAppUrl> = emptyList(),
    override val nativeAppIdDetectionMethods: List<DetectionMethod.NativeAppId> = emptyList()
  ): Deal(activationUrls, webAppUrlDetectionMethods, nativeAppIdDetectionMethods) {
    data class Info(override val storeName: String, override val description: String): Deal.Info(storeName, description)
  }
}
