package com.shoppingassistanceservices.ui.temp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.reflect.TypeToken
import com.shoppingassistanceservices.R
import com.shoppingassistanceservices.core.util.SerializationUtils.defaultSerializer
import com.shoppingassistanceservices.core.util.UrlRefiningUtils
import com.shoppingassistanceservices.data.model.dealapplicationconfig.webapp.coupon.CouponApplicationWebAppConfig
import com.shoppingassistanceservices.data.repository.CouponApplicationWebAppConfigRepository
import com.shoppingassistanceservices.databinding.FragmentTempDataPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TempDataFragment : Fragment(), DataSetUpdatingFragment {
  private lateinit var viewBinding: FragmentTempDataPageBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    viewBinding = FragmentTempDataPageBinding.inflate(inflater, container, false)

    viewBinding.configSourceSwitch.visibility = View.VISIBLE
    viewBinding.configSourceSwitch.isChecked = requireContext().getSharedPreferences("tempsp", Context.MODE_PRIVATE).getBoolean("configSourceBtb", true)
    viewBinding.configSourceSwitch.setOnCheckedChangeListener { _, b ->
      requireContext().getSharedPreferences("tempsp", Context.MODE_PRIVATE).edit().putBoolean("configSourceBtb", b).apply()
    }

    update()

    viewBinding.floatingActionButton.setOnClickListener {
      val bsDialog = BottomSheetDialog(requireContext())

      val view = LayoutInflater.from(requireContext()).inflate(R.layout.layout_temp_data_interface_item_actions_bottom_sheet_dialog, null, false)
      view.findViewById<Button>(R.id.addButton).setOnClickListener {
        val text = view.findViewById<EditText>(R.id.addEditText)?.text?.toString()

        if (!text.isNullOrEmpty() && text.isNotBlank()) {
          try {
            val sp = requireContext().getSharedPreferences("tempsp", Context.MODE_PRIVATE)

            sp.getString("couponApplicationWebAppConfig_serialized", null).apply {
              val raw: MutableList<CouponApplicationWebAppConfig> = if (this != null) defaultSerializer.fromJson<List<CouponApplicationWebAppConfig>?>(this, object : TypeToken<List<CouponApplicationWebAppConfig>>() {}.type).toMutableList() else mutableListOf()

              val newItems: List<CouponApplicationWebAppConfig> = defaultSerializer.fromJson(text, object : TypeToken<List<CouponApplicationWebAppConfig>>() {}.type)
              newItems.forEach {
                it.url = UrlRefiningUtils.trim(it.url)
              }

              raw.addAll(newItems)

              sp.edit().putString("couponApplicationWebAppConfig_serialized", defaultSerializer.toJson(raw)).apply()
            }
          } catch (thr: Throwable) {
            Toast.makeText(requireContext(), thr.localizedMessage, Toast.LENGTH_SHORT).show()
            thr.printStackTrace()
          }
        }

        bsDialog.dismiss()
        update()
      }

      bsDialog.setContentView(view)
      bsDialog.show()
    }

    return viewBinding.root
  }

  override fun update() {
    val raw = CouponApplicationWebAppConfigRepository.getItems(requireContext())

    val couponApplicationWebAppConfigsData = raw.map { TempDataItem(defaultSerializer.toJson(it)) }

    if (viewBinding.tempDataFragmentPageList.adapter != null)
      (viewBinding.tempDataFragmentPageList.adapter as TempDataInterfaceItemAdapter).apply {
        items = couponApplicationWebAppConfigsData.toMutableList()
        notifyDataSetChanged()
      }
    else
      viewBinding.tempDataFragmentPageList.adapter = TempDataInterfaceItemAdapter(requireContext(), couponApplicationWebAppConfigsData.toMutableList()) {
        val adapter = (viewBinding.tempDataFragmentPageList.adapter as TempDataInterfaceItemAdapter)
        adapter.items.remove(it)

        requireContext().getSharedPreferences("tempsp", Context.MODE_PRIVATE).edit().putString("couponApplicationWebAppConfig_serialized", defaultSerializer.toJson(adapter.items)).apply()

        update()
      }

    if (couponApplicationWebAppConfigsData.isNotEmpty()) {
      viewBinding.tempDataFragmentPageNoItemsText.visibility = View.GONE
    } else
      viewBinding.tempDataFragmentPageNoItemsText.visibility = View.VISIBLE
  }
}