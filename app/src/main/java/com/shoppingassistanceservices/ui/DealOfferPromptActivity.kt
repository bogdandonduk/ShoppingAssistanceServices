package com.shoppingassistanceservices.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shoppingassistanceservices.R
import com.shoppingassistanceservices.core.bY4zqVb3zoS2ImB.CoreService
import com.shoppingassistanceservices.core.bridge.*
import com.shoppingassistanceservices.core.dealapplication.webapp.coupon.CouponApplicationWebAppProcessStage
import com.shoppingassistanceservices.databinding.ActivityDealOfferPromptBinding

class DealOfferPromptActivity : AppCompatActivity() {
  private lateinit var viewBinding: ActivityDealOfferPromptBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val intent = intent ?: return

    isShown = true

    viewBinding = ActivityDealOfferPromptBinding.inflate(layoutInflater)

    setContentView(viewBinding.root)

    val storeName = intent.getStringExtra(KEY_NAME_STORE)

    val modal = BottomSheetDialog(this)

    modal.setOnCancelListener {
      finish()
    }

    when (intent.getStringExtra(KEY_DEAL)) {
      VALUE_DEAL_COUPON -> {
        when (intent.getStringExtra(KEY_MODAL_TYPE)) {
          VALUE_MODAL_TYPE_COUPONS_FOUND -> {
            modal.setContentView(R.layout.layout_modal_bottom_sheet_coupons_found)
            modal.findViewById<TextView>(R.id.layout_popup_coupons_found_store_name_text_view)?.text = storeName

            modal.findViewById<TextView>(R.id.layout_popup_coupons_found_hint_text_view)?.text = "${intent.getIntExtra(KEY_COUPON_COUNT, -1)} ${getString(R.string.coupons_found)}"

            modal.findViewById<Button>(R.id.layout_popup_coupons_found_continue_button)?.setOnClickListener {
              modal.dismiss()

              try {
                BottomSheetDialog(this).apply {
                  setOnDismissListener {
                    finish()
                  }
                  setOnCancelListener {
                    finish()
                  }

                  setContentView(R.layout.layout_modal_bottom_sheet_coupon_application_commence)

                  findViewById<Button>(R.id.layout_popup_coupon_application_commence_continue_button)?.setOnClickListener {
                    CoreService.instance?.also { coreService ->
                      coreService.couponApplicationWebAppWorktopProcessStage = CouponApplicationWebAppProcessStage.Injection(intent.getStringExtra(KEY_NEXT_PROMO_CODE)!!)
                    }

                    dismiss()
                  }
                }.show()
              } catch (thr: Throwable) {
                thr.printStackTrace()
              }
            }

            modal.findViewById<View>(R.id.layout_popup_coupons_found_cancel_image_button)?.setOnClickListener {
              modal.dismiss()
              finish()
            }
          }
          VALUE_MODAL_TYPE_COUPON_APPLIED -> {
            modal.setContentView(R.layout.layout_modal_bottom_sheet_coupon_applied)
            modal.findViewById<TextView>(R.id.layout_popup_coupon_applied_store_name_text_view)?.text = storeName

            modal.findViewById<TextView>(R.id.layout_popup_coupon_applied_amount_saved_hint_text_view)?.text = "$${intent.getDoubleExtra(KEY_SUM_SAVED, -1.0)}"
            modal.findViewById<TextView>(R.id.layout_popup_coupon_applied_original_price_value_text_view)?.text = "$${intent.getDoubleExtra(KEY_PRICE_ORIGINAL, -1.0)}"
            modal.findViewById<TextView>(R.id.layout_popup_coupon_applied_with_app_help_value_text_view)?.text = "$${intent.getDoubleExtra(KEY_PRICE_AFTER_APPLICATION, -1.0)}"
            modal.findViewById<TextView>(R.id.layout_popup_coupon_applied_coupon_used_value_text_view)?.text = "${intent.getStringExtra(KEY_COUPON_USED)}"

            modal.findViewById<View>(R.id.layout_popup_coupon_applied_continue_button)?.setOnClickListener {
              modal.dismiss()
              finish()
            }
          }
          VALUE_MODAL_TYPE_COUPON_NOT_APPLIED-> {
            modal.setContentView(R.layout.layout_modal_bottom_sheet_coupon_not_applied)

            modal.findViewById<View>(R.id.layout_popup_coupon_not_applied_continue_button)?.setOnClickListener {
              modal.dismiss()
              finish()
            }
          }
          else -> Unit
        }
      }
      else -> Unit
    }

    try {
      modal.show()
    } catch (thr: Throwable) {
      thr.printStackTrace()
    }
  }

  override fun onDestroy() {
    super.onDestroy()

    isShown = false
  }

  override fun onPause() {
    super.onPause()

    finish()
  }

  companion object {
    @Volatile
    var isShown = false
  }
}