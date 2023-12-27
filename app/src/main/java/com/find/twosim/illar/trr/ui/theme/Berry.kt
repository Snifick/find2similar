package com.find.twosim.illar.trr.ui.theme

import androidx.compose.runtime.MutableState

data class Berry(
    val imageId:Int,
    val isVisible:MutableState<Boolean>,
    val alwaysActive:MutableState<Boolean>
)
