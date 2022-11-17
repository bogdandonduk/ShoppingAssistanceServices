package com.shoppingassistanceservices.bridge

import android.annotation.SuppressLint
import android.content.Context
import com.shoppingassistanceservices.bridge.core.state.State
import com.shoppingassistanceservices.bridge.core.state.StateClock

class ShoppingAssistanceServicesBridge private constructor(internal val context: Context) {
  val isInstalled: State<Boolean> = State(null)
  val isRunning: State<Boolean> = State(null)

  init {
    StateClock.start()
  }

  companion object {
    @SuppressLint("StaticFieldLeak")
    internal var instance: ShoppingAssistanceServicesBridge? = null

    @JvmStatic
    @JvmOverloads
    fun getSingleton(context: Context, new: Boolean = false): ShoppingAssistanceServicesBridge {
      if (new)
        instance = null

      return instance ?: synchronized(this) {
        instance = ShoppingAssistanceServicesBridge(context)
        instance!!
      }
    }

    @JvmStatic
    fun deleteSingleton() {
      instance = null
      StateClock.stop()
    }
  }
}