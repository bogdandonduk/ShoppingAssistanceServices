package com.shoppingassistanceservices.core.extensions

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.shoppingassistanceservices.data.model.dealapplicationconfig.webapp.coupon.CouponApplicationWebAppConfigNodeInfo

val AccessibilityNodeInfo.allChildrenFlat: List<AccessibilityNodeInfo>
  get() {
    val allChildrenFlat = mutableListOf<AccessibilityNodeInfo>()

    traverseChildrenTree(allChildrenFlat)

    return allChildrenFlat.toList()
  }

private fun AccessibilityNodeInfo.traverseChildrenTree(collectToList: MutableList<AccessibilityNodeInfo>) {
  collectToList.add(this)

  if (childCount == 0) return

  for (i in 0 until childCount)
    getChild(i)?.also {
      it.traverseChildrenTree(collectToList)
    }
}

fun AccessibilityNodeInfo.findAccessibilityNodeInfosByContentDescription(contentDescription: CharSequence): List<AccessibilityNodeInfo> {
  val matchingChildren = mutableListOf<AccessibilityNodeInfo>()

  matchingChildren.addAll(allChildrenFlat.filter { it.contentDescription == contentDescription })

  return matchingChildren.toList()
}

fun AccessibilityNodeInfo.findAccessibilityNodeInfos(
  className: CharSequence,
  viewIdResourceName: CharSequence?,
  excludeViewIdResourceNames: List<CharSequence> = emptyList(),
  text: CharSequence?,
  excludeTexts: List<CharSequence> = emptyList(),
  contentDescription: CharSequence?,
  excludeContentDescriptions: List<CharSequence> = emptyList(),
  succeedingNodeInfos: List<CouponApplicationWebAppConfigNodeInfo>?
): List<AccessibilityNodeInfo> {
  val matchingChildren = mutableListOf<AccessibilityNodeInfo>()

  allChildrenFlat.forEach { accessibilityNodeInfo ->
    if (
      accessibilityNodeInfo.className == className
      && (
          (!excludeViewIdResourceNames.contains(accessibilityNodeInfo.viewIdResourceName) && accessibilityNodeInfo.viewIdResourceName != null && viewIdResourceName != null && accessibilityNodeInfo.viewIdResourceName.toString() == viewIdResourceName.toString())
              || (!excludeTexts.contains(accessibilityNodeInfo.text) && accessibilityNodeInfo.text != null && text != null && Regex(text.toString()).containsMatchIn(accessibilityNodeInfo.text.toString()) && accessibilityNodeInfo.matchPosition(succeedingNodeInfos ?: emptyList(), allChildrenFlat))
              || (!excludeContentDescriptions.contains(accessibilityNodeInfo.contentDescription) && accessibilityNodeInfo.contentDescription != null && contentDescription != null && Regex(contentDescription.toString()).containsMatchIn(accessibilityNodeInfo.contentDescription) && accessibilityNodeInfo.matchPosition(succeedingNodeInfos ?: emptyList(), allChildrenFlat))
          )
    )
      matchingChildren.add(accessibilityNodeInfo)
  }

  return matchingChildren.toList()
}

private fun AccessibilityNodeInfo.matchPosition(succeedingNodeInfos: List<CouponApplicationWebAppConfigNodeInfo>, allNodes: List<AccessibilityNodeInfo>): Boolean {
  if (succeedingNodeInfos.isEmpty())
    return true

  val matches = mutableListOf<Boolean>()

  val log = text != null && text.toString() == "\$250.00"
  try {
    allNodes.indexOf(this).also { index ->
      if (index != -1)
        for (i in succeedingNodeInfos.indices) {
          val succeedingNodeInfo = succeedingNodeInfos[i]
          val succeedingNode = allNodes[(index + 1) + i]

          matches.add(
            (
                succeedingNodeInfo.className == succeedingNode.className
                && (
                    (succeedingNodeInfo.id != null && succeedingNode.viewIdResourceName != null && succeedingNodeInfo.id == succeedingNode.viewIdResourceName)
                    || (succeedingNodeInfo.text != null && succeedingNode.text != null && Regex(succeedingNodeInfo.text.toString()).containsMatchIn(succeedingNode.text.toString()))
                    || (succeedingNode.contentDescription != null && succeedingNode.contentDescription != null && Regex(succeedingNodeInfo.contentDescription.toString()).containsMatchIn(succeedingNode.contentDescription.toString()))
                )
            )
          )
        }
    }
  } catch (thr: Throwable) {
    matches.add(false)
    thr.printStackTrace()
  }

  return matches.isNotEmpty() && !matches.contains(false)
}



