package com.shoppingassistanceservices.core.extensions

import kotlin.random.Random

fun <T> Random.nextItem(items: List<T>): T? {
  if (items.isEmpty()) return null

  val randomIndex = Random.nextInt(items.size)

  return items[randomIndex]
}