package com.alpenraum.shimstack.common

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity

fun Context.getActivity(): AppCompatActivity? =
    when (this) {
        is AppCompatActivity -> this
        is ContextWrapper -> baseContext.getActivity()
        else -> null
    }

// fun <T> NavController<T>.popBackstackOrNavigate(destination: T) {
//    if (!this.moveToTop { it == destination }) {
//        this.navigate(destination)
//    }
// }