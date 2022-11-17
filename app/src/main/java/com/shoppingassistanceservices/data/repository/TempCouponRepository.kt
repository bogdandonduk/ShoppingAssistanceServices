package com.shoppingassistanceservices.data.repository

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.shoppingassistanceservices.core.util.SerializationUtils.defaultSerializer
import com.shoppingassistanceservices.data.model.dealapplicationconfig.temp.TempCoupon

internal object TempCouponRepository {
  // temp
  fun getItems(context: Context): List<TempCoupon> {
    val serializedCoupons = context.getSharedPreferences("tempsp", Context.MODE_PRIVATE).getString("tempCoupon_serialized", null)

    serializedCoupons ?: return emptyList()

    return try {
      defaultSerializer.fromJson(serializedCoupons, object : TypeToken<List<TempCoupon>>() {}.type)
    } catch (thr: Throwable) {
      emptyList()
    }
  }
}