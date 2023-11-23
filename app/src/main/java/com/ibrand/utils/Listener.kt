package com.ibrand.utils

/**
 * Use this to send any type of data as inline listener
 */
class SendSingleItemListener<T>(val item: (item: T) -> Unit) {
    fun sendItem(item: T) = item(item)
}

class SendDoubleItemsListener<E, T>(val item: (item: E, type: T) -> Unit) {
    fun sendItem(item: E, type: T) = item(item, type)
}

class SendMinusOrPLusItemsListener<T>(val item: (item: T) -> Unit) {
    fun sendItem(item: T) = item(item)
}

interface CallbackActionInterface {
    fun sendCallback()
}