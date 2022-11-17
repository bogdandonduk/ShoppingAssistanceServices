package com.shoppingassistanceservices.data.model.dealapplicationconfig.temp

import com.google.gson.annotations.SerializedName

internal data class TempCoupon(
  @SerializedName("url")
  var targetUrl: String,
  val promoCodes: List<String> = emptyList()
)