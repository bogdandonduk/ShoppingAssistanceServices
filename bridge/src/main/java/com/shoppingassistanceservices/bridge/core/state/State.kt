package com.shoppingassistanceservices.bridge.core.state


class State<T> internal constructor(initialValue: T?) {
  private val observers: MutableList<Observer<T>> by lazy {
    mutableListOf()
  }

  fun addObserver(observer: Observer<T>) {
    if (!observers.contains(observer)) {
      observers.add(observer)

      value?.let {
        observer.onChange(it)
      }
    }
  }

  fun removeObserver(observer: Observer<T>) {
    observers.remove(observer)
  }

  private var value: T? = initialValue

  internal fun postValue(newValue: T, postDifferent: Boolean = true) {
    if (!postDifferent || newValue != value) {
      value = newValue

      observers.forEach {
        it.onChange(value!!)
      }
    }
  }

  fun interface Observer<T> {
    fun onChange(newValue: T)
  }
}