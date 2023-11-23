package com.ibrand.utils

import android.content.Context
import android.view.accessibility.AccessibilityManager


object AccessibilityUtils {
    fun isAccessibilityEnabled(context: Context): Boolean {
        val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        return accessibilityManager.isTouchExplorationEnabled
    }
}