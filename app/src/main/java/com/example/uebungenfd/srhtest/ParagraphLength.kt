package com.example.uebungenfd.srhtest

import androidx.annotation.StringRes
import com.example.uebungenfd.R

enum class LengthValue(

    @StringRes
    val textId: Int,
    val apiValue: String,

) {

    Short(1, "short"),
    Medium(2, "medium"),
    Long(3, "long"),

    /*
    Short(R.string.short, "short"),
    Medium(R.string.medium, "medium"),
    Long(R.string.long, "long"),
    */

}
