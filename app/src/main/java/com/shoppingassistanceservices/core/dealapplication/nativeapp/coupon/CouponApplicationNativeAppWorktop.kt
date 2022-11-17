package com.shoppingassistanceservices.core.dealapplication.nativeapp.coupon

import com.shoppingassistanceservices.data.model.dealapplicationconfig.webapp.coupon.CouponApplicationWebAppConfigNodeInfo

class CouponApplicationNativeAppWorktop(
  val url: String,
  val maximizationControls: Map<CouponApplicationWebAppConfigNodeInfo, Boolean>,
  val promoCodes: Map<String, Double>,
  var maximizationControlAttemptStartTime: Long,
  var promoCodeInjectionAttemptStartTime: Long
)