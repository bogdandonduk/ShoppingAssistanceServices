package com.shoppingassistanceservices.core.dealapplication.webapp.coupon.worktop

import com.shoppingassistanceservices.core.dealapplication.webapp.coupon.CouponApplicationWebAppProcessStage
import com.shoppingassistanceservices.data.model.dealapplicationconfig.webapp.coupon.CouponApplicationWebAppConfig

class CouponApplicationWebAppWorktopImpl: CouponApplicationWebAppWorktop {
  override var couponApplicationWebAppWorktopProcessStage: CouponApplicationWebAppProcessStage = CouponApplicationWebAppProcessStage.Detection
  override var couponApplicationWebAppWorktopAppId: CharSequence? = null
  override var couponApplicationWebAppWorktopUrl: CharSequence? = null
  override var couponApplicationWebAppWorktopBaseUrl: CharSequence? = null
  override val couponApplicationWebAppWorktopPromoCodes: MutableMap<String, Double?> = mutableMapOf()
  override var couponApplicationWebAppWorktopPromoCodeConcluded: Pair<String, Double>? = null
  override var couponApplicationWebAppWorktopPriceBefore: Double? = null
  override var couponApplicationWebAppWorktopConfig: CouponApplicationWebAppConfig? = null




}