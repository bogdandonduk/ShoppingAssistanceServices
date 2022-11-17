package com.shoppingassistanceservices.core.dealapplication.nativeapp.coupon

import com.shoppingassistanceservices.data.model.dealapplicationconfig.webapp.coupon.CouponApplicationWebAppConfig

sealed class CouponApplicationNativeAppStage {
  object Detection: CouponApplicationNativeAppStage()

  object InitialEvaluation: CouponApplicationNativeAppStage()

  class Injection(promoCode: String, config: CouponApplicationWebAppConfig): CouponApplicationNativeAppStage()

  object PostInjectionEvaluation : CouponApplicationNativeAppStage()

  class Activation(promoCode: String, config: CouponApplicationWebAppConfig): CouponApplicationNativeAppStage()

  object PostActivationEvaluation : CouponApplicationNativeAppStage()

  object Maximization : CouponApplicationNativeAppStage()

  object PostMaximizationEvaluation : CouponApplicationNativeAppStage()
}