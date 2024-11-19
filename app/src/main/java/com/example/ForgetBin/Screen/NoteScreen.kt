package com.example.ForgetBin.Screen

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ForgetBin.R
import com.example.ForgetBin.ViewModel.DataViewModel
import com.example.ForgetBin.database.ThoughtData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun NoteScreen(
    navController: NavController,
    dataViewModel: DataViewModel,
    id: Int?,
    title: String?,
    description: String?,
    date: String?,

    ) {
    val context = LocalContext.current
    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }
    var currentDate: String = remember {
        SimpleDateFormat(
            "d MMM yyyy, hh:mm a", Locale.getDefault()
        ).format(Date())
    }

    var desState = remember { mutableStateOf("") }
    var titleState = remember { mutableStateOf("") }

    if (id != null) {
        desState = remember { mutableStateOf(description!!) }
        titleState = remember { mutableStateOf(title!!) }
    }
    Box(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(Color.White),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier
                        .padding(top = 9.dp)
                        .clickable {
                            vibrator.vibrate(
                                VibrationEffect.createOneShot(
                                    15, VibrationEffect.DEFAULT_AMPLITUDE
                                )
                            )
                            navController.navigate("mainScreen") {
                                popUpTo("mainScreen") { inclusive = true }
                            }
                        }

                    ) {
                        Image(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "menu",
                            modifier = Modifier.size(30.dp)
                        )
                    }


                    if (id != null) {
                        Box(
                            modifier = Modifier.padding(start = 40.dp, top = 9.dp)
                        ) {
                            DeleteIconScreen(id, dataViewModel, navController, vibrator, context)
                        }
                    }


                    if (titleState.value != "" || desState.value != "") {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd

                        ) {
                            Image(painter = painterResource(R.drawable.tick__2_),
                                contentDescription = "AddNotes",
                                modifier = Modifier
                                    .background(Color.Transparent)
                                    .size(50.dp)
                                    .padding(end = 20.dp)
                                    .clickable {
                                        vibrator.vibrate(
                                            VibrationEffect.createOneShot(
                                                15, VibrationEffect.DEFAULT_AMPLITUDE
                                            )
                                        )
                                        if (id != null) {
                                            dataViewModel.update(
                                                ThoughtData(
                                                    id,
                                                    titleState.value,
                                                    desState.value,
                                                    currentDate
                                                )
                                            )
                                            Toast
                                                .makeText(
                                                    context, "Update Thought", Toast.LENGTH_SHORT
                                                )
                                                .show()

                                            navController.navigate("mainScreen") {
                                                popUpTo("mainScreen") { inclusive = true }
                                            }
                                        } else {
                                            dataViewModel.insert(
                                                ThoughtData(
                                                    0, titleState.value, desState.value, currentDate
                                                )
                                            )
                                            Toast
                                                .makeText(
                                                    context, "Save Thought", Toast.LENGTH_SHORT
                                                )
                                                .show()
                                            navController.navigate("mainScreen") {
                                                popUpTo("mainScreen") { inclusive = true }
                                            }
                                        }
                                    })
                        }
                    }

                }
            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(top = 100.dp, start = 5.dp, end = 5.dp)
        ) {


            TextField(value = titleState.value,
                onValueChange = { titleState.value = it },
                placeholder = {
                    Text(
                        text = "Title", fontSize = 22.sp
                    )
                },
                textStyle = TextStyle(
                    fontSize = 22.sp,

                    ),

                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),

                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(1f)
            )


            if (id != null) {
                currentDate = remember {
                    SimpleDateFormat(
                        "d MMM yyyy, hh:mm a", Locale.getDefault()
                    ).format(Date())
                }
            }
            Text(
                text = currentDate,
                modifier = Modifier.padding(start = 18.dp, top = 5.dp),
                fontSize = 12.sp,
                color = Color(0xCC717171)

            )

            TextField(value = desState.value,
                onValueChange = { desState.value = it },
                placeholder = {
                    Text(
                        text = "Description", fontSize = 16.sp
                    )
                },
                textStyle = TextStyle(
                    fontSize = 16.sp
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent

                ),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 15.dp)


            )
        }
    }
}