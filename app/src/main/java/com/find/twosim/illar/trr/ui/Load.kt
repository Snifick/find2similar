package com.find.twosim.illar.trr.ui

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.find.twosim.illar.trr.R
import kotlinx.coroutines.delay

@Composable
fun Load( goNext:()->Unit){

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Image(painter = painterResource(id = R.drawable.back), contentDescription = "",
            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
        )
        val infiti = rememberInfiniteTransition()
        val anim = infiti.animateFloat(initialValue = 0.95f, targetValue = 1.0f, animationSpec = InfiniteRepeatableSpec(
            tween(500,0, LinearEasing), repeatMode = RepeatMode.Reverse
        ), label = ""
        )
        LaunchedEffect(key1 = Unit, block = {
            delay(1250)
            goNext.invoke()
        })
        Text(text = "Loading", modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(32.dp)
            .scale(anim.value),
            fontSize = 38.sp,
            color = Color(0xFF252525),
            fontFamily = FontFamily.Monospace,
        )
    }
}