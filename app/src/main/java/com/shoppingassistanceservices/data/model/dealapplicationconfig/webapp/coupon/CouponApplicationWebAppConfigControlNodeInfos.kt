package com.shoppingassistanceservices.data.model.dealapplicationconfig.webapp.coupon

import com.google.gson.annotations.SerializedName

data class CouponApplicationWebAppConfigControlNodeInfos(
  @SerializedName("promo")
  val promoCodeInputNode: CouponApplicationWebAppConfigNodeInfo,
  @SerializedName("total")
  val totalPriceTextNode: CouponApplicationWebAppConfigNodeInfo
)