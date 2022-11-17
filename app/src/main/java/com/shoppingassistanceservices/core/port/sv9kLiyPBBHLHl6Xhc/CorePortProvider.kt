package com.shoppingassistanceservices.core.port.sv9kLiyPBBHLHl6Xhc

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.database.CharArrayBuffer
import android.database.ContentObserver
import android.database.Cursor
import android.database.DataSetObserver
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import com.shoppingassistanceservices.core.bY4zqVb3zoS2ImB.CoreService
import com.shoppingassistanceservices.core.bridge.SAS_STATE_IS_INSTALLED
import com.shoppingassistanceservices.core.bridge.SAS_STATE_IS_RUNNING
import com.shoppingassistanceservices.core.bridge.SAS_URI_STRING_CORE_PORT_PROVIDER_STATE
import com.shoppingassistanceservices.core.extensions.TAG

class CorePortProvider : ContentProvider() {
  override fun onCreate(): Boolean {
    return true
  }

  override fun query(
    uri: Uri,
    projection: Array<out String>?,
    selection: String?,
    selectionArgs: Array<out String>?,
    sortOrder: String?
  ): Cursor? {

    return when (uri.toString()) {
      SAS_URI_STRING_CORE_PORT_PROVIDER_STATE -> {
        object : Cursor {
          override fun close() {}

          override fun getCount(): Int = 0

          override fun getPosition(): Int = 0

          override fun move(p0: Int): Boolean = false

          override fun moveToPosition(p0: Int): Boolean = false

          override fun moveToFirst(): Boolean = false

          override fun moveToLast(): Boolean = false

          override fun moveToNext(): Boolean = false

          override fun moveToPrevious(): Boolean = false

          override fun isFirst(): Boolean = false

          override fun isLast(): Boolean = false

          override fun isBeforeFirst(): Boolean = false

          override fun isAfterLast(): Boolean = false

          override fun getColumnIndex(p0: String?): Int = 0

          override fun getColumnIndexOrThrow(p0: String?): Int = 0

          override fun getColumnName(p0: Int): String = ""

          override fun getColumnNames(): Array<String> = emptyArray()

          override fun getColumnCount(): Int = 0

          override fun getBlob(p0: Int): ByteArray = ByteArray(0)

          override fun getString(p0: Int): String = ""

          override fun copyStringToBuffer(p0: Int, p1: CharArrayBuffer?) {}

          override fun getShort(p0: Int): Short = 0

          override fun getInt(p0: Int): Int = 4673

          override fun getLong(p0: Int): Long = 0

          override fun getFloat(p0: Int): Float = 0f

          override fun getDouble(p0: Int): Double = 0.0

          override fun getType(p0: Int): Int = Cursor.FIELD_TYPE_NULL

          override fun isNull(p0: Int): Boolean = false

          override fun deactivate() {}

          override fun requery(): Boolean = false

          override fun isClosed(): Boolean = false

          override fun registerContentObserver(p0: ContentObserver?) {}

          override fun unregisterContentObserver(p0: ContentObserver?) {}

          override fun registerDataSetObserver(p0: DataSetObserver?) {}

          override fun unregisterDataSetObserver(p0: DataSetObserver?) {}

          override fun setNotificationUri(p0: ContentResolver?, p1: Uri?) {}

          override fun getNotificationUri(): Uri = Uri.EMPTY

          override fun getWantsAllOnMoveCalls(): Boolean = false

          override fun setExtras(p0: Bundle?) {}

          override fun getExtras(): Bundle = bundleOf(
            SAS_STATE_IS_INSTALLED to true,
            SAS_STATE_IS_RUNNING to (CoreService.instance != null)
          )

          override fun respond(p0: Bundle?): Bundle = bundleOf()
        }
      }

      else -> null
    }
  }

  override fun getType(uri: Uri): String? {
    return null
  }

  override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
    return null
  }

  override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

  override fun update(uri: Uri, contentValues: ContentValues?, where: String?, selectionArgs: Array<out String>?): Int {
    return 0
  }
}