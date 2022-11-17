package com.shoppingassistanceservices.data.model.dealapplicationconfig.nativeapp.coupon

import com.google.gson.annotations.SerializedName

data class CouponApplicationNativeAppConfig(
  var url: String,
  @SerializedName("controls")
  val nodeControls: CouponApplicationNativeAppConfigControlNodeInfos,
  @SerializedName("apply")
  val applyPromoCodeButtonNode: List<CouponApplicationNativeAppConfigNodeInfo> = emptyList(),
  @SerializedName("remove")
  val removePromoCodeButtonNode: List<CouponApplicationNativeAppConfigNodeInfo> = emptyList(),
  @SerializedName("resize")
  val shouldRequestDesktopPage: Boolean = false,
  @SerializedName("unlock")
  val unlockControlsNode: List<CouponApplicationNativeAppConfigNodeInfo> = emptyList(),
  @SerializedName("pageLoadedMarkers")
  val pageLoadedMarkerNodes: List<CouponApplicationNativeAppConfigNodeInfo> = emptyList(),
  @SerializedName("promoCodeAppliedMarkers")
  val promoCodeAppliedMarkerNodes: List<CouponApplicationNativeAppConfigNodeInfo> = emptyList(),
  @SerializedName("promoCodeNotFoundMarkers")
  val promoCodeNotFoundMarkerNodes: List<CouponApplicationNativeAppConfigNodeInfo> = emptyList()
)