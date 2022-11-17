package com.shoppingassistanceservices.core.logging

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.gson.reflect.TypeToken
import com.shoppingassistanceservices.core.util.SerializationUtils.defaultSerializer

class DealOfferTimeLogManager private constructor(application: Application) {
  private var couponDealOfferTimeLogs: MutableMap<String, Long>
  private var cashbackDealOfferTimeLogs: MutableMap<String, Long>

  private val sp = application.getSharedPreferences(NAME_SP, Context.MODE_PRIVATE)
  private val spEditor = sp.edit()

  init {
    couponDealOfferTimeLogs =
      try {
        defaultSerializer.fromJson(
          sp.getString(KEY_SP_COUPON_DEAL_OFFER_TIME_LOGS, null),
          object : TypeToken<Map<String, Long>>() {}.type
        )
      } catch (thr: Throwable) {
        mutableMapOf()
      }


    cashbackDealOfferTimeLogs =
      try {
        defaultSerializer.fromJson(
          sp.getString(KEY_SP_CASHBACK_DEAL_OFFER_TIME_LOGS, null),
          object : TypeToken<Map<String, Long>>() {}.type
        )
      } catch (thr: Throwable) {
        mutableMapOf()
      }
  }

  private fun updateCouponDealOfferTimeLogs() {
    couponDealOfferTimeLogs =
      try {
        defaultSerializer.fromJson(
          sp.getString(KEY_SP_COUPON_DEAL_OFFER_TIME_LOGS, null),
          object : TypeToken<Map<String, Long>>() {}.type
        )
      } catch (thr: Throwable) {
        mutableMapOf()
      }
  }

  private fun updateCashbackDealOfferTimeLogs() {
    cashbackDealOfferTimeLogs =
      try {
        defaultSerializer.fromJson(
          sp.getString(KEY_SP_CASHBACK_DEAL_OFFER_TIME_LOGS, null),
          object : TypeToken<Map<String, Long>>() {}.type
        )
      } catch (thr: Throwable) {
        mutableMapOf()
      }
  }

  private fun addCouponDealOfferTimeLogs(vararg urlTimePairs: Pair<String, Long>) {
    urlTimePairs.takeIf { it.isNotEmpty() }?.apply {
      urlTimePairs.forEach {
        couponDealOfferTimeLogs[it.first] = it.second
      }

      spEditor.putString(KEY_SP_COUPON_DEAL_OFFER_TIME_LOGS, defaultSerializer.toJson(couponDealOfferTimeLogs)).apply()
    }
  }

  private fun addCashbackDealOfferTimeLogs(vararg urlTimePairs: Pair<String, Long>) {
    urlTimePairs.takeIf { it.isNotEmpty() }?.apply {
      urlTimePairs.forEach {
        cashbackDealOfferTimeLogs[it.first] = it.second
      }

      spEditor.putString(KEY_SP_CASHBACK_DEAL_OFFER_TIME_LOGS, defaultSerializer.toJson(cashbackDealOfferTimeLogs)).apply()
    }
  }

  fun getCouponDealOfferTimeLog(url: String): Long? = couponDealOfferTimeLogs[url]
  fun getCashbackDealOfferTimeLog(url: String): Long? = cashbackDealOfferTimeLogs[url]

  companion object {
    @SuppressLint("StaticFieldLeak")
    @Volatile
    private var instance: DealOfferTimeLogManager? = null

    fun getSingleton(application: Application) =
      instance ?: synchronized(this) {
        instance = DealOfferTimeLogManager(application)
        instance!!
      }

    private val NAME_SP = "DealOfferLogs"
    private val KEY_SP_COUPON_DEAL_OFFER_TIME_LOGS = "CouponDealOfferTimeLogs_serialized"
    private val KEY_SP_CASHBACK_DEAL_OFFER_TIME_LOGS = "CashbackDealOfferTimeLogs_serialized"
  }
}