package com.shoppingassistanceservices.core.dealapplication.webapp.coupon

sealed class CouponApplicationWebAppProcessStage {
  object Detection: CouponApplicationWebAppProcessStage()

  object Maximization: CouponApplicationWebAppProcessStage()

  object Injection: CouponApplicationWebAppProcessStage()

  object Activation: CouponApplicationWebAppProcessStage()

  object Evaluation : CouponApplicationWebAppProcessStage()
}