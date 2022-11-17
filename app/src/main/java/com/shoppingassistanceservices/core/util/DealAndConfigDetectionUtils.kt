package com.shoppingassistanceservices.core.util

import com.shoppingassistanceservices.bridge.model.deal.Deal
import com.shoppingassistanceservices.core.extensions.nextItem
import com.shoppingassistanceservices.data.model.dealapplicationconfig.webapp.coupon.CouponApplicationWebAppConfig
import kotlin.random.Random

object DealAndConfigDetectionUtils {
  fun detectGetRandomCouponDealCouponApplicationWebAppConfigMatch(trimmedUrl: String, deals: List<Deal.Coupon>, configs: List<CouponApplicationWebAppConfig>): Pair<Deal, CouponApplicationWebAppConfig>? {
    val pairs = mutableListOf<Pair<Deal, CouponApplicationWebAppConfig>>()

    deals.forEach { deal ->
      deal.webAppUrlDetectionMethods.forEach { webAppUrlDetectionMethod ->
        val webAppUrlRegex = Regex(webAppUrlDetectionMethod.url)

        if (webAppUrlRegex.containsMatchIn(trimmedUrl)) {
          val matchingConfig = configs.find { config ->
            Regex(config.url).containsMatchIn(webAppUrlRegex.toString())
          }

          if (matchingConfig != null) {
            pairs.add(Pair(deal, matchingConfig))
          }
        }
      }
    }

    return Random.nextItem(pairs)
  }

  fun detectGetRandomCashbackDeal(trimmedUrl: String, deals: List<Deal.Cashback>): Deal.Cashback? {
    val collectedDeals = mutableListOf<Deal.Cashback>()

    deals.forEach { deal ->
      if (
        deal.webAppUrlDetectionMethods.any { webAppUrlDetectionMethod ->
          Regex(webAppUrlDetectionMethod.url).containsMatchIn(trimmedUrl)
        }
      ) collectedDeals.add(deal)
    }

    return Random.nextItem(collectedDeals)
  }

}