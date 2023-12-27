package com.find.twosim.illar.trr.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.find.twosim.illar.trr.R
import com.find.twosim.illar.trr.ui.theme.Berry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Game(navController: NavController){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Image(painter = painterResource(id = R.drawable.stol), contentDescription = "",
            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
        )
        val images = remember{
            listOf(R.drawable.i1,R.drawable.i2, R.drawable.gi3,R.drawable.gi4,R.drawable.gi5,R.drawable.gi6,R.drawable.gi7,R.drawable.gi8,
                R.drawable.i1,R.drawable.i2, R.drawable.gi3,R.drawable.gi4,R.drawable.gi5,R.drawable.gi6,R.drawable.gi7,R.drawable.gi8).shuffled()
        }
        val items = remember{
            (0..15).mapIndexed { index, i -> Berry(images[index], mutableStateOf(true),
                mutableStateOf(false)) }
        }
        val infiti = rememberInfiniteTransition()
        val anim = infiti.animateFloat(initialValue = 0.93f, targetValue = 1.07f, animationSpec = InfiniteRepeatableSpec(
            tween(1800,0, LinearEasing), repeatMode = RepeatMode.Reverse
        ), label = ""
        )
        LaunchedEffect(key1 = Unit, block = {
            delay(1500)
            items.forEach {
                it.isVisible.value = false
            }
        })
        val tryes = remember{
            mutableStateOf(0)
        }
        Text(text = "Find pairs!", modifier = Modifier
            .align(Alignment.TopStart)
            .padding(32.dp)
            .scale(anim.value),
            fontSize = 39.sp,
            color = Color(0xFF8B5504),
            fontFamily = FontFamily.Cursive,
        )
        Text(text = "Tryes: ${tryes.value}", modifier = Modifier
            .align(Alignment.TopStart)
            .padding(32.dp)
            .padding(top = 52.dp)
            .scale(anim.value),
            fontSize = 29.sp,
            color = Color(0xFF8B5504),
            fontFamily = FontFamily.Cursive,
        )
        val text = remember{
            mutableStateOf("Tap to start!")
        }
        Text(text = "${text.value}", modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(33.dp)
            .scale(anim.value),
        fontSize = 27.sp,
        color = Color(0xFF202020),
        fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        LaunchedEffect(key1 = text.value, block = {
            delay(3000)
            text.value = "Tap to play!"
        })
        Text(text = "${text.value}", modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(32.dp)
            .scale(anim.value),
            fontSize = 27.sp,
            color = Color(0xFFFFF9F1),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )

        val currentState = remember{
            mutableStateOf(1)
        }
        val firstSelected:MutableState<Berry?> = remember{
            mutableStateOf(null)
        }
        val secondSelected:MutableState<Berry?> = remember{
            mutableStateOf(null)
        }
        val scope = rememberCoroutineScope()
        val locker = remember {
            mutableStateOf(true)
        }
        LaunchedEffect(key1 = tryes.value, block = {
            val f = items.filter { !it.alwaysActive.value }
            if(f.isEmpty()){
                text.value = "Very good! You win!"
                withContext(Dispatchers.Main){
                    navController.popBackStack()
                    navController.navigate("Game")
                }
            }

        })

        LazyVerticalGrid(columns = GridCells.Fixed(4), content = {
                items(items){
                    Box(modifier = Modifier
                        .scale(if (it == firstSelected.value || it == secondSelected.value) 1.02f else 0.94f)
                        .size(80.dp)
                        .clip(RoundedCornerShape(50))
                        .clickable(MutableInteractionSource(), null) {
                            if (locker.value) {
                                when (currentState.value) {
                                    1 -> {
                                        firstSelected.value = it
                                        firstSelected.value!!.isVisible.value = true
                                        currentState.value++
                                    }

                                    2 -> {
                                        secondSelected.value = it
                                        secondSelected.value!!.isVisible.value = true
                                        locker.value = false
                                        scope.launch {
                                            if (!firstSelected!!.value!!.alwaysActive.value && !secondSelected!!.value!!.alwaysActive.value)
                                                if (firstSelected.value!!.imageId == secondSelected!!.value!!.imageId) {
                                                    text.value = "Right"

                                                    firstSelected!!.value!!.alwaysActive.value =
                                                        true
                                                    secondSelected!!.value!!.alwaysActive.value =
                                                        true
                                                    firstSelected!!.value!!.isVisible.value = true
                                                    secondSelected!!.value!!.isVisible.value = true
                                                    delay(900)
                                                } else {
                                                    text.value = "Ohh... Nope!"
                                                    delay(900)
                                                    firstSelected!!.value!!.isVisible.value = false
                                                    secondSelected!!.value!!.isVisible.value = false
                                                    firstSelected.value = null
                                                    secondSelected.value = null
                                                }

                                            currentState.value = 1
                                            tryes.value++
                                            locker.value = true
                                        }


                                    }
                                }
                            }

                        }, contentAlignment = Alignment.Center){
                        Image(painter = painterResource(id = R.drawable.kvadratik), contentDescription = "",
                            modifier = Modifier.fillMaxSize())
                        AnimatedVisibility(visible = it.isVisible.value,
                            enter =
                            fadeIn(tween(200)),
                            exit =
                        fadeOut(tween(200))) {
                            Image(painter = painterResource(id = it.imageId), contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp))
                        }

                    }
                }
                                                                 
        },
            modifier = Modifier.size(320.dp))
        
    }

}