package com.shoppingassistanceservices.ui.temp

import android.content.Context
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.shoppingassistanceservices.core.util.SerializationUtils.defaultSerializer
import com.shoppingassistanceservices.databinding.LayoutTempDataItemBinding

class TempDataInterfaceItemAdapter(val context: Context, var items: MutableList<TempDataItem>, val removeAction: (tempDataItem: TempDataItem) -> Unit) : RecyclerView.Adapter<TempDataInterfaceItemAdapter.ViewHolder>() {
  private lateinit var layoutInflater: LayoutInflater

  lateinit var vibrator: Vibrator

  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    super.onAttachedToRecyclerView(recyclerView)

    layoutInflater = LayoutInflater.from(context)

    vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
  }

  inner class ViewHolder(val viewBinding: LayoutTempDataItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    lateinit var contentItem: TempDataItem

    init {
      viewBinding.root.setOnLongClickListener {
        vibrator.vibrate(50)

        if (this::contentItem.isInitialized)
          removeAction(contentItem)

        false
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutTempDataItemBinding.inflate(layoutInflater, parent, false))

  override fun getItemCount(): Int = items.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.contentItem = items[position]

    holder.viewBinding.layoutTempDataItemText.text = defaultSerializer.toJson(holder.contentItem.text.replace("\"", ""))
  }
}