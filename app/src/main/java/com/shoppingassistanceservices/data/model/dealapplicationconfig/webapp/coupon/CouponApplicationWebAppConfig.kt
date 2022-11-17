package com.shoppingassistanceservices.data.model.dealapplicationconfig.webapp.coupon

import com.google.gson.annotations.SerializedName

data class CouponApplicationWebAppConfig(
  var url: String,
  @SerializedName("controls")
  val nodeControls: CouponApplicationWebAppConfigControlNodeInfos,
  @SerializedName("apply")
  val applyPromoCodeNodes: List<CouponApplicationWebAppConfigNodeInfo>?,
  @SerializedName("remove")
  val removePromoCodeNodes: List<CouponApplicationWebAppConfigNodeInfo>? = null,
  @SerializedName("resize")
  val changeViewport: Boolean?,
  @SerializedName("unlock")
  val maximizeControlsNodes: List<CouponApplicationWebAppConfigNodeInfo>? = null,
  @SerializedName("pageLoadedMarkers")
  val pageLoadedMarkerNodes: List<CouponApplicationWebAppConfigNodeInfo>? = null,
  @SerializedName("promoCodeAppliedMarkers")
  val promoCodeAppliedMarkerNodes: List<CouponApplicationWebAppConfigNodeInfo>? = null,
  @SerializedName("promoCodeNotFoundMarkers")
  val promoCodeNotFoundMarkerNodes: List<CouponApplicationWebAppConfigNodeInfo>? = null,
)