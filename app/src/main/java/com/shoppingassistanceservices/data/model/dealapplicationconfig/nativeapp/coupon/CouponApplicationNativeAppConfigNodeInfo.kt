package com.shoppingassistanceservices.data.model.dealapplicationconfig.nativeapp.coupon

import com.google.gson.annotations.SerializedName

data class CouponApplicationNativeAppConfigNodeInfo(
  val id: String?,
  @SerializedName("androidClass")
  val className: String,
  val content: List<String> = emptyList(),
  val timeout: Long = 1000L,
  @SerializedName("precedingElements")
  val precedingNodes: List<CouponApplicationNativeAppConfigNodeInfo> = emptyList(),
  @SerializedName("succeedingElements")
  val succeedingNodes: List<CouponApplicationNativeAppConfigNodeInfo> = emptyList()
)