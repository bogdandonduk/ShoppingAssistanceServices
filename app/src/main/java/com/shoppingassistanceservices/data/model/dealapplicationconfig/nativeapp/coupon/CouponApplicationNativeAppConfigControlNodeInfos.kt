package com.shoppingassistanceservices.data.model.dealapplicationconfig.nativeapp.coupon

import com.google.gson.annotations.SerializedName

data class CouponApplicationNativeAppConfigControlNodeInfos(
  @SerializedName("promo")
  val promoCodeInputNode: CouponApplicationNativeAppConfigNodeInfo,
  @SerializedName("total")
  val totalPriceTextNode: CouponApplicationNativeAppConfigNodeInfo
)