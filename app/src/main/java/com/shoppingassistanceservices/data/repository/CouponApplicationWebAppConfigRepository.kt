package com.shoppingassistanceservices.data.repository

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.shoppingassistanceservices.core.util.SerializationUtils.defaultSerializer
import com.shoppingassistanceservices.data.model.dealapplicationconfig.webapp.coupon.CouponApplicationWebAppConfig

object CouponApplicationWebAppConfigRepository {
  // temp
  fun getItems(context: Context): List<CouponApplicationWebAppConfig> {
    val serializedConfigs = context.getSharedPreferences("tempsp", Context.MODE_PRIVATE).getString("couponApplicationWebAppConfig_serialized", null)

    serializedConfigs ?: return emptyList()

    return try {
      defaultSerializer.fromJson(serializedConfigs, object : TypeToken<List<CouponApplicationWebAppConfig>>() {}.type)
    } catch (thr: Throwable) {
      emptyList()
    }
  }
}