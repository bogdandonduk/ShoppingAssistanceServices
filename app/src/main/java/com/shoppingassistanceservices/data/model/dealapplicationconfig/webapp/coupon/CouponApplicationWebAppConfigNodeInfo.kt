package com.shoppingassistanceservices.data.model.dealapplicationconfig.webapp.coupon

import com.google.gson.annotations.SerializedName

data class CouponApplicationWebAppConfigNodeInfo(
  val id: String?,
  @SerializedName("androidClass")
  val className: String,
  val text: String?,
  val contentDescription: String?,
  val timeout: Long? = null,
  @SerializedName("preceding")
  val precedingNodes: List<CouponApplicationWebAppConfigNodeInfo>? = null,
  @SerializedName("succeeding")
  val succeedingNodes: List<CouponApplicationWebAppConfigNodeInfo>? = null
)