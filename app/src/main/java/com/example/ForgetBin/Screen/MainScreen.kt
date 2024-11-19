package com.example.ForgetBin.Screen

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ForgetBin.R
import com.example.ForgetBin.ViewModel.DataViewModel
import com.example.ForgetBin.database.ThoughtData
import java.time.LocalTime


@Composable
fun listShow(
    dataViewModel: DataViewModel,
    navController: NavController,
    vibrator: Vibrator,
) {

    val data = dataViewModel.getData.observeAsState(emptyList())

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(10.dp),
        flingBehavior = ScrollableDefaults.flingBehavior(),


        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(data.value.reversed()) {
            ItemList(it.id, it.title, it.description, it.Date, navController, vibrator)
        }
    }

}


@Composable
fun MainScreen(navController: NavController, dataViewModel: DataViewModel, id: Int?) {
    val context = LocalContext.current
    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }

    val colors = listOf(
        Color(0xffBFD641),
        Color(0xFF9BB7D4),
        Color(0xFFF5DF4D),
        Color(0xffE0B589),
        Color(0xFF56C6A9),
        Color(0xFFD19C97),
        Color(0xFFD2C29D),
        Color(0xFF6F9FD8),
        Color(0xFFEC9787),
        Color(0xFFB4B7BA),
        Color(0xFFC0AB8E),
        Color(0xFFBE9EC9),
        Color(0xFF95DEE3),
        Color(0xFFEDCDC2),
        Color(0xFF92A8D1),
        Color(0xFF9896A4),
        Color(0xFFD8AE47),
        Color(0xFFCCCCFF),
        Color(0xFF84FFFF),
        Color(0xFFA5D6A7),
        Color(0xFFB39DDB),
        Color(0xFFC5E1A5),
        Color(0xFFE6EE9C),
        Color(0xFFFFE082),
    )
    val currentDate = remember { LocalTime.now() }
    val hourCycle = currentDate.hour
    val backgroundColor = colors[hourCycle % colors.size]


    Box(
        modifier = Modifier.fillMaxSize(1f), contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier
                .padding(top = 50.dp, start = 5.dp, end = 5.dp)
                .fillMaxSize(1f)
                .clip(
                    RoundedCornerShape(
                        15.dp
                    )
                )

        ) {
            Box(
                modifier = Modifier
                    .background(backgroundColor)
                    .clip(
                        RoundedCornerShape(
                            30.dp
                        )
                    )
                    .fillMaxWidth(1f)
                    .size(height = 50.dp, width = 100.dp)
                    .padding(start = 10.dp),
                contentAlignment = Alignment.CenterStart
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(R.drawable.menu),
                        contentDescription = "menu",
                        modifier = Modifier
                            .size(25.dp)
                            .clickable {
                                vibrator.vibrate(
                                    VibrationEffect.createOneShot(
                                        15, VibrationEffect.DEFAULT_AMPLITUDE
                                    )
                                )
                                Toast
                                    .makeText(
                                        context, "It will work after an Update", Toast.LENGTH_SHORT
                                    )
                                    .show()
                                    .toString()
                            }

                    )
                    Text(
                        text = "ForgetHub",
                        fontSize = 25.sp,
                        color = Color.Black,
                        fontWeight = FontWeight(300),
                        modifier = Modifier.padding(start = 40.dp)
                    )

                    if (id != null) {
                        DeleteIconScreen(id, dataViewModel, navController, vibrator, context)
                    }

                }

            }


            if (dataViewModel.getData.observeAsState(emptyList()).value.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .background(Color.Transparent)
                        .padding(top = 100.dp), contentAlignment = Alignment.TopCenter
                ) {
                    Image(
                        painter = painterResource(R.drawable.backimage),
                        contentDescription = "BackImage",
                        modifier = Modifier
                            .size(width = 350.dp, 430.dp)
                            .background(Color.Transparent)

                    )
                }
            } else {
                listShow(dataViewModel, navController, vibrator)
            }

        }

        Column(
            modifier = Modifier
                .padding(bottom = 70.dp, end = 40.dp)
                .clip(
                    RoundedCornerShape(
                        15.dp,
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .background(backgroundColor)
                    .size(50.dp)
                    .clickable {
                        vibrator.vibrate(
                            VibrationEffect.createOneShot(
                                15, VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )
                        navController.navigate("NoteScreen") {}
                    }, contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.add),
                    contentDescription = "AddNotes",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
fun DeleteIconScreen(
    id: Int,
    dataViewModel: DataViewModel,
    navController: NavController,
    vibrator: Vibrator,
    context: Context,
) {

    Box(
        modifier = Modifier.fillMaxWidth(.7f), contentAlignment = Alignment.CenterEnd
    ) {

        Image(painter = painterResource(R.drawable.deleteicon),
            contentDescription = "AddNotes",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            15, VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                    dataViewModel.delete(
                        ThoughtData(
                            id, "deleteTitle", "deleteDescription", "deleteDate"
                        )
                    )
                    Toast
                        .makeText(context, "Thought Delete", Toast.LENGTH_SHORT)
                        .show()
                    navController.popBackStack("mainScreen", inclusive = false)
//                        popUpTo("mainScreen") { inclusive = true }

                })

    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemList(
    id: Int,
    title: String,
    description: String,
    date: String,
    navController: NavController,
    vibrator: Vibrator,
) {

    val changeColor = remember { mutableStateOf(Color.White) }
    Box(
        modifier = Modifier
            .padding(top = 15.dp)
            .clip(
                RoundedCornerShape(15.dp),
            )
            .combinedClickable(onClick = {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        15, VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
                navController.navigate(route = "screen/$id/$title/$description/$date") {
                    popUpTo("screen/$id/$title/$description/$date") { inclusive = true }
                }

            }, onLongClick = {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        15, VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
                navController.navigate(route = "deleteIcon/$id") {}
                changeColor.value = Color.Cyan

            }


            )
            .border(1.dp, Color(0xCC717171), RoundedCornerShape(15.dp))
            .background(changeColor.value),

        ) {

        Column(
            modifier = Modifier.padding(10.dp)

        ) {
            Text(
                text = title,
                fontSize = 20.sp,
            )

            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xCC717171),
                modifier = Modifier.padding(top = 10.dp)
            )

            Text(
                text = date.toString(),
                fontSize = 10.sp,
                color = Color(0xCC717171),
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}


