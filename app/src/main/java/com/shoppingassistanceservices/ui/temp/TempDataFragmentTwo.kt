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
import com.shoppingassistanceservices.data.model.dealapplicationconfig.temp.TempCoupon
import com.shoppingassistanceservices.data.repository.TempCouponRepository
import com.shoppingassistanceservices.databinding.FragmentTempDataPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TempDataFragmentTwo : Fragment(), DataSetUpdatingFragment {
  private lateinit var viewBinding: FragmentTempDataPageBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    viewBinding = FragmentTempDataPageBinding.inflate(inflater, container, false)

    viewBinding.configSourceSwitch.visibility = View.GONE

    update()

    viewBinding.floatingActionButton.setOnClickListener {
      val bsDialog = BottomSheetDialog(requireContext())

      val view = LayoutInflater.from(requireContext()).inflate(R.layout.layout_temp_data_interface_item_actions_bottom_sheet_dialog, null, false)
      view.findViewById<Button>(R.id.addButton).setOnClickListener {
        val text = view.findViewById<EditText>(R.id.addEditText)?.text?.toString()

        if (!text.isNullOrEmpty() && text.isNotBlank()) {
          try {
            val sp = requireContext().getSharedPreferences("tempsp", Context.MODE_PRIVATE)

            sp.getString("tempCoupon_serialized", null).apply {
              val raw: MutableList<TempCoupon> = if (this != null) defaultSerializer.fromJson<List<TempCoupon>?>(this, object : TypeToken<List<TempCoupon>>() {}.type).toMutableList() else mutableListOf()

              val newItems: List<TempCoupon> = defaultSerializer.fromJson(text, object : TypeToken<List<TempCoupon>>() {}.type)
              newItems.forEach {
                it.targetUrl = UrlRefiningUtils.trim(it.targetUrl)
              }

              raw.addAll(newItems)

              sp.edit().putString("tempCoupon_serialized", defaultSerializer.toJson(raw)).apply()
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
    val raw = TempCouponRepository.getItems(requireContext())

    val tempCouponData = raw.map { TempDataItem(defaultSerializer.toJson(it)) }

    if (viewBinding.tempDataFragmentPageList.adapter != null)
      (viewBinding.tempDataFragmentPageList.adapter as TempDataInterfaceItemAdapter).apply {
        items = tempCouponData.toMutableList()
        notifyDataSetChanged()
      }
    else
      viewBinding.tempDataFragmentPageList.adapter = TempDataInterfaceItemAdapter(requireContext(), tempCouponData.toMutableList()) {
        val adapter = (viewBinding.tempDataFragmentPageList.adapter as TempDataInterfaceItemAdapter)
        adapter.items.remove(it)

        requireContext().getSharedPreferences("tempsp", Context.MODE_PRIVATE).edit().putString("tempCoupon_serialized", defaultSerializer.toJson(adapter.items)).apply()

        update()
      }

    if (tempCouponData.isNotEmpty()) {
      viewBinding.tempDataFragmentPageNoItemsText.visibility = View.GONE
    } else
      viewBinding.tempDataFragmentPageNoItemsText.visibility = View.VISIBLE
  }
}