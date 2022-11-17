package com.shoppingassistanceservices.bridge.core.state

import android.net.Uri
import com.shoppingassistanceservices.bridge.ShoppingAssistanceServicesBridge
import com.shoppingassistanceservices.bridge.core.SAS_STATE_IS_INSTALLED
import com.shoppingassistanceservices.bridge.core.SAS_STATE_IS_RUNNING
import com.shoppingassistanceservices.bridge.core.SAS_URI_STRING_CORE_PORT_PROVIDER_STATE
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

object StateClock {
  private var stateClockJob: Job? = null

  fun start() {
    stateClockJob = CoroutineScope(IO).launch {
      while (true) {
        ShoppingAssistanceServicesBridge.instance?.apply {
          val stateUri = Uri.parse(SAS_URI_STRING_CORE_PORT_PROVIDER_STATE)

          val stateCursor = context.contentResolver.query(stateUri, null, null, null, null)
          val stateIsInstalled = stateCursor?.extras?.getBoolean(SAS_STATE_IS_INSTALLED)
          val stateIsRunning = stateCursor?.extras?.getBoolean(SAS_STATE_IS_RUNNING)
          stateCursor?.close()

          isInstalled.postValue(stateIsInstalled ?: false)
          isRunning.postValue(stateIsRunning ?: false)
        }

        delay(3000)
      }
    }
  }

  fun stop() {
    stateClockJob?.cancel()
    stateClockJob = null
  }
}