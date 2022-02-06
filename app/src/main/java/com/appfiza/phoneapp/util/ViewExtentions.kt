package com.appfiza.phoneapp.util

import android.view.View

/**
 * Created by Fayçal KADDOURI 🐈
 */

/** makes visible a view. */
fun View.visible() {
    visibility = View.VISIBLE
}

/** makes gone a view. */
fun View.gone() {
    visibility = View.GONE
}

/** makes invisible a view. */
fun View.hide() {
    visibility = View.INVISIBLE
}