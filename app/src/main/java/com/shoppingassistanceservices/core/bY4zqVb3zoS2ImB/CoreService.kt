package com.shoppingassistanceservices.core.bY4zqVb3zoS2ImB

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.os.bundleOf
import com.shoppingassistanceservices.bridge.core.DetectionMethod
import com.shoppingassistanceservices.bridge.model.deal.Deal
import com.shoppingassistanceservices.core.bridge.*
import com.shoppingassistanceservices.core.bridge.KEY_DEAL
import com.shoppingassistanceservices.core.bridge.KEY_MODAL_TYPE
import com.shoppingassistanceservices.core.bridge.KEY_NAME_STORE
import com.shoppingassistanceservices.core.bridge.VALUE_DEAL_COUPON
import com.shoppingassistanceservices.core.bridge.VALUE_MODAL_TYPE_COUPONS_FOUND
import com.shoppingassistanceservices.core.dealapplication.webapp.coupon.worktop.CouponApplicationWebAppWorktop
import com.shoppingassistanceservices.core.dealapplication.webapp.coupon.CouponApplicationWebAppProcessStage
import com.shoppingassistanceservices.core.dealapplication.webapp.coupon.worktop.CouponApplicationWebAppWorktopImpl
import com.shoppingassistanceservices.core.extensions.findAccessibilityNodeInfos
import com.shoppingassistanceservices.core.logging.DealOfferTimeLogManager
import com.shoppingassistanceservices.core.util.AppConfigDetectionUtils
import com.shoppingassistanceservices.core.util.DealAndConfigDetectionUtils
import com.shoppingassistanceservices.core.util.UrlRefiningUtils
import com.shoppingassistanceservices.data.model.appconfig.nativeapp.NativeAppConfig
import com.shoppingassistanceservices.data.model.appconfig.webapp.WebAppConfig
import com.shoppingassistanceservices.data.model.appconfig.webapp.WebAppConfigDetectionMethod
import com.shoppingassistanceservices.data.model.dealapplicationconfig.webapp.coupon.CouponApplicationWebAppConfig
import com.shoppingassistanceservices.data.repository.CouponApplicationWebAppConfigRepository
import com.shoppingassistanceservices.data.repository.NativeAppConfigRepository
import com.shoppingassistanceservices.data.repository.TempCouponRepository
import com.shoppingassistanceservices.data.repository.WebAppConfigRepository
import com.shoppingassistanceservices.ui.DealOfferPromptActivity

internal class CoreService : AccessibilityService(),
  CouponApplicationWebAppWorktop by CouponApplicationWebAppWorktopImpl() {
  private var tempCouponDeals = emptyList<Deal.Coupon>()
  internal fun tempUpdateTempCouponDeals() {
    tempCouponDeals = TempCouponRepository.getItems(this).map {
      Deal.Coupon(
        promoCodes = it.promoCodes,
        webAppUrlDetectionMethods = listOf(
          DetectionMethod.WebAppUrl(it.targetUrl)
        ),
      )
    }
  }

  private var tempCouponApplicationWebAppConfigs = emptyList<CouponApplicationWebAppConfig>()
  internal fun tempUpdateTempCouponApplicationWebAppConfigs() {
    tempCouponApplicationWebAppConfigs = CouponApplicationWebAppConfigRepository.getItems(this)
  }

  private val dealOfferLogManager by lazy {
    DealOfferTimeLogManager.getSingleton(application)
  }

  override fun onServiceConnected() {
    super.onServiceConnected()

    tempUpdateTempCouponApplicationWebAppConfigs()
    tempUpdateTempCouponDeals()
  }

  override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    val appId = event?.packageName ?: return
    val detectedAppConfig = AppConfigDetectionUtils.detectAppConfig(
      appId,
      WebAppConfigRepository,
      NativeAppConfigRepository
    ) ?: return

    when (detectedAppConfig) {
      is WebAppConfig -> {
        when (detectedAppConfig.detectionMethod) {
          is WebAppConfigDetectionMethod.DetectByUrl -> {
            val contentRoot = event.source ?: return
            val windowContentRoot = contentRoot.window?.root ?: return

            val urlWidgetNodeText =
              windowContentRoot.findAccessibilityNodeInfosByViewId(detectedAppConfig.detectionMethod.urlWidgetId)
                .singleOrNull()?.text?.toString()
            val fullUrl = urlWidgetNodeText?.run { UrlRefiningUtils.trim(urlWidgetNodeText) }
            val baseUrl = fullUrl?.run { UrlRefiningUtils.trimToBase(fullUrl, false) }

            baseUrl?.apply {
              dealOfferLogManager.getCashbackDealOfferTimeLog(this)?.apply {
                if (this + 5000 < System.currentTimeMillis()) {
                  // val cashbackDeal = DealAndConfigDetectionUtils.detectGetRandomCashbackDeal(baseUrl, emptyList()) ?: return

                  // cashback deal offer logic
                  return
                }
              }
            }

            when (couponApplicationWebAppWorktopProcessStage) {
              is CouponApplicationWebAppProcessStage.Detection -> {
                baseUrl ?: return

                val (couponDeal, couponApplicationConfig) = DealAndConfigDetectionUtils.detectGetRandomCouponDealCouponApplicationWebAppConfigMatch(
                  fullUrl,
                  tempCouponDeals,
                  tempCouponApplicationWebAppConfigs
                ) ?: return

                initializeCouponApplicationWebAppWorktop(
                  if (!couponApplicationConfig.maximizeControlsNodes.isNullOrEmpty())
                    CouponApplicationWebAppProcessStage.Maximization
                  else
                    CouponApplicationWebAppProcessStage.Injection,
                  appId = appId,
                  url = fullUrl,
                  baseUrl = baseUrl,
                  promoCodes = couponDeal.activationTokens.associateWith { null }.toMutableMap(),
                  config = couponApplicationConfig,
                  priceBefore = windowContentRoot.findAccessibilityNodeInfos(
                    couponApplicationConfig.nodeControls.totalPriceTextNode.className,
                    couponApplicationConfig.nodeControls.totalPriceTextNode.id,
                    text = couponApplicationConfig.nodeControls.totalPriceTextNode.text,
                    contentDescription = couponApplicationConfig.nodeControls.totalPriceTextNode.contentDescription,
                    succeedingNodeInfos = couponApplicationConfig.nodeControls.totalPriceTextNode.succeedingNodes,
                  ).takeIf { it.isNotEmpty() }?.first().run {
                    this?.text?.run {
                      this.filter {
                        it == '.' || (try {
                          Integer.parseInt(it.toString())
                        } catch (thr: Throwable) {
                          null
                        }) != null
                      }.takeIf { it.isNotEmpty() }?.toString()?.toDoubleOrNull()
                    }
                  }
                )

                val nextPromoCode =
                  couponApplicationWebAppWorktopPromoCodes.entries.find { it.value == null }!!.key

                startActivity(
                  Intent(this, DealOfferPromptActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK

                    putExtra(KEY_DEAL, VALUE_DEAL_COUPON)
                    putExtra(KEY_MODAL_TYPE, VALUE_MODAL_TYPE_COUPONS_FOUND)

                    putExtra(KEY_NAME_STORE, baseUrl)
                    putExtra(KEY_COUPON_COUNT, couponApplicationWebAppWorktopPromoCodes.size)
                    putExtra(KEY_NEXT_PROMO_CODE, nextPromoCode)
                  }
                )
              }

              is CouponApplicationWebAppProcessStage.Injection -> {
                val promoInputNode = windowContentRoot.findAccessibilityNodeInfos(
                  className = couponApplicationWebAppWorktopConfig!!.nodeControls.promoCodeInputNode.className,
                  viewIdResourceName = couponApplicationWebAppWorktopConfig!!.nodeControls.promoCodeInputNode.id,
                  excludeViewIdResourceNames = listOf(detectedAppConfig.detectionMethod.urlWidgetId),
                  text = couponApplicationWebAppWorktopConfig!!.nodeControls.promoCodeInputNode.text,
                  contentDescription = couponApplicationWebAppWorktopConfig!!.nodeControls.promoCodeInputNode.contentDescription,
                  succeedingNodeInfos = couponApplicationWebAppWorktopConfig!!.nodeControls.promoCodeInputNode.succeedingNodes,
                ).takeIf { it.isNotEmpty() }?.first()

                promoInputNode?.apply {
                  val promoCode =
                    (couponApplicationWebAppWorktopProcessStage as CouponApplicationWebAppProcessStage.Injection).promoCode

                  performAction(
                    AccessibilityNodeInfo.ACTION_SET_TEXT,
                    bundleOf(
                      AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE to promoCode
                    )
                  )

                  couponApplicationWebAppWorktopProcessStage =
                    CouponApplicationWebAppProcessStage.Activation(promoCode)
                }
              }

              is CouponApplicationWebAppProcessStage.Activation -> {
                val applyButtonNode = windowContentRoot.findAccessibilityNodeInfos(
                  couponApplicationWebAppWorktopConfig.applyPromoCodeNodes.first().className,
                  couponApplicationWebAppWorktopConfig.applyPromoCodeButtonNode.id,
                  text = couponApplicationWebAppWorktopConfig.applyPromoCodeButtonNode.text,
                  contentDescription = couponApplicationWebAppWorktopConfig.applyPromoCodeButtonNode.contentDescription,
                  succeedingNodeInfos = couponApplicationWebAppWorktopConfig.applyPromoCodeButtonNode.succeedingNodes,
                ).takeIf { it.isNotEmpty() }?.first()

                applyButtonNode?.apply {
                  performAction(AccessibilityNodeInfo.ACTION_CLICK)

                  couponApplicationWebAppWorktopProcessStage =
                    CouponApplicationWebAppProcessStage.Evaluation(
                      (couponApplicationWebAppWorktopProcessStage as CouponApplicationWebAppProcessStage.Activation).promoCode,
                      System.currentTimeMillis()
                    )
                }
              }

              is CouponApplicationWebAppProcessStage.Evaluation -> {
                if (couponApplicationWebAppWorktop!!.promoCodeChosen != null) {
                  startActivity(
                    Intent(this, DealOfferPromptActivity::class.java).apply {
                      flags = Intent.FLAG_ACTIVITY_NEW_TASK

                      putExtra(KEY_DEAL, VALUE_DEAL_COUPON)
                      putExtra(KEY_MODAL_TYPE, VALUE_MODAL_TYPE_COUPON_APPLIED)

                      putExtra(KEY_NAME_STORE, couponApplicationWebAppWorktop!!.baseUrl)

                      val originalPrice = couponApplicationWebAppWorktop!!.originalPrice
                      val priceAfterApplication = couponApplicationWebAppWorktop?.promoCodes?.get(
                        couponApplicationWebAppWorktop!!.promoCodeChosen
                      )

                      putExtra(
                        KEY_SUM_SAVED,
                        if (originalPrice != null && priceAfterApplication != null) originalPrice - priceAfterApplication else null
                      )
                      putExtra(KEY_PRICE_ORIGINAL, originalPrice)
                      putExtra(KEY_PRICE_AFTER_APPLICATION, priceAfterApplication)
                      putExtra(
                        KEY_COUPON_USED,
                        (couponApplicationWebAppWorktopProcessStage as CouponApplicationWebAppProcessStage.Evaluation).promoCode
                      )
                    }
                  )

                  couponApplicationWebAppWorktopProcessStage =
                    CouponApplicationWebAppProcessStage.Detection
                  couponApplicationWebAppWorktop = null

                  return
                }

                val promoCodeAppliedMarkerNode = windowContentRoot.findAccessibilityNodeInfos(
                  couponApplicationWebAppWorktop!!.config.promoCodeAppliedMarkerNodes.className,
                  couponApplicationWebAppWorktop!!.config.promoCodeAppliedMarkerNodes.id,
                  text = couponApplicationWebAppWorktop!!.config.promoCodeAppliedMarkerNodes.text,
                  contentDescription = couponApplicationWebAppWorktop!!.config.promoCodeAppliedMarkerNodes.contentDescription,
                  succeedingNodeInfos = couponApplicationWebAppWorktop!!.config.promoCodeAppliedMarkerNodes.succeedingNodes,
                ).takeIf { it.isNotEmpty() }?.first()

                if (promoCodeAppliedMarkerNode != null) {
                  val totalPriceNode = windowContentRoot.findAccessibilityNodeInfos(
                    couponApplicationWebAppWorktop!!.config.nodeControls.totalPriceTextNode.className,
                    couponApplicationWebAppWorktop!!.config.nodeControls.totalPriceTextNode.id,
                    text = couponApplicationWebAppWorktop!!.config.nodeControls.totalPriceTextNode.text,
                    contentDescription = couponApplicationWebAppWorktop!!.config.nodeControls.totalPriceTextNode.contentDescription,
                    succeedingNodeInfos = couponApplicationWebAppWorktop!!.config.nodeControls.totalPriceTextNode.succeedingNodes,
                  ).takeIf { it.isNotEmpty() }?.first()

                  totalPriceNode?.text?.apply {
                    val extractedPrice = this.filter {
                      it == '.' || (try {
                        Integer.parseInt(it.toString())
                      } catch (thr: Throwable) {
                        null
                      }) != null
                    }.takeIf { it.isNotEmpty() }?.toString()?.toDoubleOrNull()

                    extractedPrice?.apply {
                      couponApplicationWebAppWorktop!!.promoCodes[(couponApplicationWebAppWorktopProcessStage as CouponApplicationWebAppProcessStage.Evaluation).promoCode] =
                        extractedPrice

                      couponApplicationWebAppWorktop!!.config.removePromoCodeButtonNode?.also { removeNodeInfo ->
                        val removeButtonNode = windowContentRoot.findAccessibilityNodeInfos(
                          removeNodeInfo.className,
                          removeNodeInfo.id,
                          text = removeNodeInfo.text,
                          contentDescription = removeNodeInfo.contentDescription,
                          succeedingNodeInfos = removeNodeInfo.succeedingNodes,
                        ).takeIf { it.isNotEmpty() }?.first()

                        removeButtonNode?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                      }

                      val nextPromoCode =
                        couponApplicationWebAppWorktop!!.promoCodes.entries.find { it.value == null }?.key

                      if (nextPromoCode != null)
                        couponApplicationWebAppWorktopProcessStage =
                          CouponApplicationWebAppProcessStage.Injection(nextPromoCode)
                      else {
                        var smallestPricePromoCodePair: Pair<String, Double>? = null

                        couponApplicationWebAppWorktop!!.promoCodes.forEach { (promoCode, priceAfterApplication) ->
                          if (smallestPricePromoCodePair == null || priceAfterApplication!! < smallestPricePromoCodePair!!.second)
                            smallestPricePromoCodePair = Pair(promoCode, priceAfterApplication!!)
                        }

                        if (smallestPricePromoCodePair != null) {
                          couponApplicationWebAppWorktopProcessStage =
                            CouponApplicationWebAppProcessStage.Injection(smallestPricePromoCodePair!!.first)
                          couponApplicationWebAppWorktop!!.promoCodeChosen =
                            smallestPricePromoCodePair!!.first
                        } else {
                          startActivity(
                            Intent(this@CoreService, DealOfferPromptActivity::class.java).apply {
                              flags = Intent.FLAG_ACTIVITY_NEW_TASK

                              putExtra(KEY_DEAL, VALUE_DEAL_COUPON)
                              putExtra(KEY_MODAL_TYPE, VALUE_MODAL_TYPE_COUPON_NOT_APPLIED)

                              putExtra(KEY_NAME_STORE, couponApplicationWebAppWorktop!!.baseUrl)
                            }
                          )

                          couponApplicationWebAppWorktopProcessStage =
                            CouponApplicationWebAppProcessStage.Detection
                          couponApplicationWebAppWorktop = null
                        }
                      }
                    }
                  }
                } else {
                  val promoCodeNotFoundedMarkerNode = windowContentRoot.findAccessibilityNodeInfos(
                    couponApplicationWebAppWorktop!!.config.promoCodeNotFoundMarkerNodes.className,
                    couponApplicationWebAppWorktop!!.config.promoCodeNotFoundMarkerNodes.id,
                    text = couponApplicationWebAppWorktop!!.config.promoCodeNotFoundMarkerNodes.text,
                    contentDescription = couponApplicationWebAppWorktop!!.config.promoCodeNotFoundMarkerNodes.contentDescription,
                    succeedingNodeInfos = couponApplicationWebAppWorktop!!.config.promoCodeNotFoundMarkerNodes.succeedingNodes,

                    ).takeIf { it.isNotEmpty() }?.first()

                  val promoCodeNotFound =
                    promoCodeNotFoundedMarkerNode != null || (couponApplicationWebAppWorktopProcessStage as CouponApplicationWebAppProcessStage.Evaluation).activationTime + 3000 < System.currentTimeMillis()

                  if (promoCodeNotFound) {
                    couponApplicationWebAppWorktop!!.promoCodes.remove((couponApplicationWebAppWorktopProcessStage as CouponApplicationWebAppProcessStage.Evaluation).promoCode)

                    val nextPromoCode =
                      couponApplicationWebAppWorktop!!.promoCodes.entries.find { it.value == null }?.key

                    if (nextPromoCode != null) {
                      couponApplicationWebAppWorktopProcessStage =
                        CouponApplicationWebAppProcessStage.Injection(nextPromoCode)
                    } else {
                      var smallestPricePromoCodePair: Pair<String, Double>? = null

                      couponApplicationWebAppWorktop!!.promoCodes.forEach { (promoCode, priceAfterApplication) ->
                        if (smallestPricePromoCodePair == null || priceAfterApplication!! < smallestPricePromoCodePair!!.second)
                          smallestPricePromoCodePair = Pair(promoCode, priceAfterApplication!!)
                      }

                      if (smallestPricePromoCodePair != null) {
                        couponApplicationWebAppWorktopProcessStage =
                          CouponApplicationWebAppProcessStage.Injection(smallestPricePromoCodePair!!.first)
                        couponApplicationWebAppWorktop!!.promoCodeChosen =
                          smallestPricePromoCodePair!!.first
                      } else {
                        startActivity(
                          Intent(this, DealOfferPromptActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK

                            putExtra(KEY_DEAL, VALUE_DEAL_COUPON)
                            putExtra(KEY_MODAL_TYPE, VALUE_MODAL_TYPE_COUPON_NOT_APPLIED)

                            putExtra(KEY_NAME_STORE, couponApplicationWebAppWorktop!!.baseUrl)
                          }
                        )

                        couponApplicationWebAppWorktopProcessStage =
                          CouponApplicationWebAppProcessStage.Detection
                        couponApplicationWebAppWorktop = null
                      }
                    }
                  }
                }
              }

              else -> {}
            }
          }

          is WebAppConfigDetectionMethod.DetectByContent -> {}
        }
      }

      is NativeAppConfig -> {

      }
    }
  }

  override fun onInterrupt() {}

  override fun onCreate() {
    super.onCreate()

    instance = this
  }

  override fun onDestroy() {
    super.onDestroy()

    instance = null
  }

  companion object {
    @Volatile
    internal var instance: CoreService? = null
  }
}