package com.example.uebungenfd.srhtest

import androidx.annotation.StringRes
import com.example.uebungenfd.R

enum class LengthValue(

    @StringRes val textId: Int,
    val apiValue: String,

) {
    Short(R.string.short, "short"),
    Medium(R.string.medium, "medium"),
    Long(R.string.long, "long"),
}
