package com.shoppingassistanceservices

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.shoppingassistanceservices.core.extensions.TAG
import com.shoppingassistanceservices.data.model.dealapplicationconfig.webapp.coupon.CouponApplicationWebAppConfig
import com.shoppingassistanceservices.data.model.dealapplicationconfig.webapp.coupon.CouponApplicationWebAppConfigControlNodeInfos
import com.shoppingassistanceservices.data.model.dealapplicationconfig.webapp.coupon.CouponApplicationWebAppConfigNodeInfo
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
internal class ShoppingAssistanceServices : Application() {
  override fun onCreate() {
    super.onCreate()

    val configs = listOf(
      CouponApplicationWebAppConfig(
        url = "https://reebok.com/.*/cart",
        nodeControls = CouponApplicationWebAppConfigControlNodeInfos(
          promoCodeInputNode = CouponApplicationWebAppConfigNodeInfo(
            id = "coupon-input",
            className = "android.widget.EditText",
            succeedingNodes = listOf(
              CouponApplicationWebAppConfigNodeInfo(
                id = "coupon-input_label",
                className = "android.view.View",
                text = "Enter your promo code",
                contentDescription = null,
              )
            ),
            text = "",
            contentDescription = null
          ),
          totalPriceTextNode = CouponApplicationWebAppConfigNodeInfo(
            id = null,
            className = "android.widget.TextView",
            text = "\\\$.*\\..*",
//            precedingNodes = listOf(
//              CouponApplicationWebAppConfigNodeInfo(
//                id = null,
//                className = "android.widget.TextView",
//                text = "TOTAL",
//                contentDescription = null,
//              ),
//              CouponApplicationWebAppConfigNodeInfo(
//                id = null,
//                className = "android.widget.TextView",
//                text = " ",
//                contentDescription = null,
//              )
//            ),
            succeedingNodes = listOf(
              CouponApplicationWebAppConfigNodeInfo(
                id = null,
                className = "android.view.View",
                text = "",
                contentDescription = null,
              ),
              CouponApplicationWebAppConfigNodeInfo(
                id = null,
                className = "android.widget.Button",
                text = "Checkout",
                contentDescription = null,
              ),
            ),
            contentDescription = null
          )
        ),
        applyPromoCodeNodes = CouponApplicationWebAppConfigNodeInfo(
          id = null,
          className = "android.widget.Button",
          text = "Apply",
          contentDescription = null,
          precedingNodes = listOf(
            CouponApplicationWebAppConfigNodeInfo(
              id = null,
              text = "Verification by ID.me",
              contentDescription = null,
              className = "android.widget.Button"
            )
          )
        ),
        promoCodeAppliedMarkerNodes = CouponApplicationWebAppConfigNodeInfo(
          id = null,
          className = "android.widget.TextView",
          text = "PROMO CODE APPLIED",
          contentDescription = null
        ),
        promoCodeNotFoundMarkerNodes = CouponApplicationWebAppConfigNodeInfo(
          id = null,
          className = "android.widget.TextView",
          text = "Coupon code .* is unknown.",
          contentDescription = null
        ),
        removePromoCodeNodes = CouponApplicationWebAppConfigNodeInfo(
          id = null,
          className = "android.widget.Button",
          text = "",
          contentDescription = null,
          succeedingNodes = listOf(
            CouponApplicationWebAppConfigNodeInfo(
              id = null,
              className = "android.view.View",
              text = "",
              contentDescription = null,
            ),
            CouponApplicationWebAppConfigNodeInfo(
              id = null,
              className = "android.widget.TextView",
              text = "ORDER SUMMARY",
              contentDescription = null,
            ),
          ),
        )
      )
    )

    Log.d(TAG, "onCreate 2: ${Gson().toJson(configs)}")
    Log.d(TAG, "onCreate 3: ${Regex("\\$.*\\..*").containsMatchIn("TOTAL (8 items) $250")}")
  }
}