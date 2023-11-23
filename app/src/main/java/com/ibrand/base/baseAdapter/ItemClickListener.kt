package com.ibrand.base.baseAdapter

interface ItemClickListener {
    fun <T> onItemClick(item: T,type:Int?=null)
     // fun <T> onItemClick(item: T,type:Int)
}