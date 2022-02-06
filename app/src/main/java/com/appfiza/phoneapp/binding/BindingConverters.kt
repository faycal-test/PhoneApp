package com.appfiza.phoneapp.binding

import android.view.View
import androidx.databinding.BindingConversion

/**
 * Created by Fayçal KADDOURI 🐈
 */

/**
 * The loading.value is a boolean and the visibility attribute takes an integer
 * (VISIBLE, GONE and INVISIBLE respectively), so we use this converter.
 *
 * There is no need to specify that this converter should be used. [BindingConversion]s are
 * applied automatically.
 */

object ConverterUtil {
    @JvmStatic fun isNullOrEmpty(str: String?): Boolean {
        return str.isNullOrEmpty()
    }
}

object BindingConverters {

    @BindingConversion
    @JvmStatic
    fun booleanToVisibility(isVisible: Boolean): Int {
        return if (isVisible) View.VISIBLE else View.GONE
    }
}
