package com.shoppingassistanceservices.core.dealapplication.webapp.coupon.worktop

import com.shoppingassistanceservices.core.dealapplication.webapp.coupon.CouponApplicationWebAppProcessStage
import com.shoppingassistanceservices.data.model.dealapplicationconfig.webapp.coupon.CouponApplicationWebAppConfig

interface CouponApplicationWebAppWorktop {
  var couponApplicationWebAppWorktopProcessStage: CouponApplicationWebAppProcessStage

  var couponApplicationWebAppWorktopAppId: CharSequence?
  var couponApplicationWebAppWorktopUrl: CharSequence?
  var couponApplicationWebAppWorktopBaseUrl: CharSequence?
  val couponApplicationWebAppWorktopPromoCodes: MutableMap<String, Double?>
  var couponApplicationWebAppWorktopPromoCodeConcluded: Pair<String, Double>?
  var couponApplicationWebAppWorktopPriceBefore: Double?
  var couponApplicationWebAppWorktopConfig: CouponApplicationWebAppConfig?

  fun initializeCouponApplicationWebAppWorktop(
    stage: CouponApplicationWebAppProcessStage,
    appId: CharSequence?,
    url: CharSequence?,
    baseUrl: CharSequence?,
    promoCodes: MutableMap<String, Double?>,
    priceBefore: Double?,
    config: CouponApplicationWebAppConfig?
  ) {
    synchronized(this) {
      couponApplicationWebAppWorktopProcessStage = stage

      couponApplicationWebAppWorktopAppId = appId
      couponApplicationWebAppWorktopUrl = url
      couponApplicationWebAppWorktopBaseUrl = baseUrl
      couponApplicationWebAppWorktopPromoCodes.apply {
        clear()
        putAll(promoCodes)
      }
      couponApplicationWebAppWorktopPriceBefore = priceBefore
      couponApplicationWebAppWorktopConfig = config
    }
  }

  fun invalidateCouponApplicationWebAppWorktop(stage: CouponApplicationWebAppProcessStage) {
    synchronized(this) {
      couponApplicationWebAppWorktopProcessStage = stage
      couponApplicationWebAppWorktopAppId = null
      couponApplicationWebAppWorktopUrl = null
      couponApplicationWebAppWorktopBaseUrl = null
      couponApplicationWebAppWorktopPromoCodes.clear()
      couponApplicationWebAppWorktopPromoCodeConcluded = null
      couponApplicationWebAppWorktopPriceBefore = null
      couponApplicationWebAppWorktopConfig = null
    }
  }
}