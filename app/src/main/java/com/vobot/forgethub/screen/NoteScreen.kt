package com.vobot.forgethub.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vobot.forgethub.R
import com.vobot.forgethub.viewModel.DataViewModel
import com.vobot.forgethub.roomDatabase.ForgetHubData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
// it is Note screen where the user write data
fun NoteScreen(
    navController: NavController,
    dataViewModel: DataViewModel,
    id: Int?,
    title: String?,
    description: String?,
    context: Context,
    interactionSource: MutableInteractionSource,
) {

    // it take current data from system
    val currentDate: String = remember {
        SimpleDateFormat(
            "d MMM yyyy, hh:mm a", Locale.getDefault()
        ).format(Date())
    }

    var manageContinuouslyMultipleClicks by remember { mutableStateOf(true) }

    var manageMissBackDataLostAlertDialogStateInNoteScreen by remember { mutableStateOf(false) }

    var descriptionState = rememberSaveable { mutableStateOf("") }

    var titleState = rememberSaveable { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()


    /*
    it is use for show data in note screen which is already save in database,
    if user click item box which is show in home screen then the item data
    will be show in note screen.
    */
    if (id != null) {
        descriptionState = rememberSaveable { mutableStateOf(description!!) }
        titleState = rememberSaveable { mutableStateOf(title!!) }
    }


    //background Image manage on Note screen
    if (id == null) {
        val image = rememberSaveable { mutableIntStateOf(dataViewModel.image()) }
        Box(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(top = 130.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(image.intValue),
                contentDescription = "note screen background image",
                alpha = .5F,
                modifier = Modifier.fillMaxSize(1f),
            )
        }
    }


    // it manage all note screen UI
    Box(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(Color.Transparent),
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
                        .clickable(
                            enabled = manageContinuouslyMultipleClicks,
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            /*
                            when the user click item box which is show in home screen then the item data
                            will be show in note screen and if the user will be update the data which
                            show in note screen then (if) condition run and when the user will not be
                            update the data which show in note screen then (else) condition run..
                            */
                            if (titleState.value == title && description == descriptionState.value) {
                                manageContinuouslyMultipleClicks = false
                                dataViewModel.vibration()
                                dataViewModel.updateEnableDisableDeleteIconOnHomeScreenState(
                                    false
                                )
                                navController.popBackStack("homeScreen", inclusive = false)
                            } else {
                                dataViewModel.vibration()
                                dataViewModel.updateEnableDisableDeleteIconOnHomeScreenState(
                                    false
                                )
                                manageMissBackDataLostAlertDialogStateInNoteScreen = true
                            }

                        }

                    ) {
                        Image(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "back icon",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    if (id != null) {
                        Box(
                            modifier = Modifier.padding(top = 12.dp)
                        ) {
                            //this function has called from here to show share icon in note screen
                            ShareDataToOtherApps(
                                dataViewModel,
                                title,
                                description,
                                context,
                                interactionSource,
                            )
                        }
                    }

                    /*
                    this is show the delete icon in note screen when user
                    open already save data screen
                     */
                    if (id != null) {
                        Box(
                            modifier = Modifier.padding(top = 9.dp)
                        ) {

                           /*
                           In this function calling, (false) argument manage the
                           alert dialog on note screen, when the delete icon
                           click from the note screen then (dismiss alert dialog
                           in note screen and pop back to home screen)
                           */
                            DeleteIcon(
                                id,
                                dataViewModel,
                                navController,
                                false,
                                interactionSource,
                                context
                            )
                        }
                    }

                    /*
                    the (save) icon show when the user will be write data in
                    text field otherwise not show (save) icon.
                    */
                    if (titleState.value != "" || descriptionState.value != "") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 12.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Image(painter = painterResource(
                                if (id == null) {
                                    R.drawable.save
                                } else {

                                    if (titleState.value == title && description == descriptionState.value) {
                                        R.drawable.save
                                    } else {
                                        R.drawable.save_as
                                    }

                                }
                            ),
                                contentDescription = "save icon",
                                modifier = Modifier
                                    .background(Color.Transparent)
                                    .size(50.dp)
                                    .padding(end = 20.dp)
                                    .clickable(
                                        enabled = manageContinuouslyMultipleClicks,
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        dataViewModel.updateEnableDisableDeleteIconOnHomeScreenState(false)
                                        manageContinuouslyMultipleClicks = false
                                        dataViewModel.vibration()

                                        /*
                                        (if) condition run when the user not update the data
                                        (else) condition run when the user update the data
                                        */
                                        if (titleState.value == title && description == descriptionState.value) {

                                            navController.popBackStack()
                                            Toast
                                                .makeText(
                                                    context,
                                                    "You haven't updated anything",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()

                                        } else {

                                            /*
                                            (if) condition run when the user update the data and data update in database
                                            (else) condition run when the user first time save data in database
                                            */
                                            if (id != null) {

                                                dataViewModel.update(ForgetHubData(id, titleState.value, descriptionState.value, currentDate))
                                                Toast.makeText(context, "Update", Toast.LENGTH_SHORT).show()
                                                coroutineScope.launch {
                                                    navController.popBackStack(
                                                        "homeScreen",
                                                        inclusive = false
                                                    )
                                                }

                                            } else {

                                                dataViewModel.insert(ForgetHubData(0, titleState.value, descriptionState.value, currentDate))
                                                Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show()
                                                coroutineScope.launch {
                                                    navController.popBackStack(
                                                        "homeScreen",
                                                        inclusive = false
                                                    )
                                                }

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
                .fillMaxSize()
                .padding(top = 95.dp, start = 5.dp, end = 5.dp)
                .imePadding()
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(1f),
                maxLines = 5,
                value = titleState.value,
                onValueChange = { titleState.value = it },
                placeholder = { Text( text = "Title", fontSize = 22.sp) },
                textStyle = TextStyle(fontSize = 22.sp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )

            Text(
                text = currentDate,
                modifier = Modifier.padding(start = 18.dp),
                fontSize = 12.sp,
                color = Color(0xCC717171)
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth(1f),
                value = descriptionState.value,
                onValueChange = { descriptionState.value = it },
                placeholder = { Text(text = "Description", fontSize = 16.sp) },
                textStyle = TextStyle(fontSize = 16.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )
        }


        /*
        (if) condition run when data state not change
        (else) condition run when data state change
        */
        if (titleState.value == title && description == descriptionState.value) {

            BackHandler() {
                dataViewModel.updateEnableDisableDeleteIconOnHomeScreenState(false)
                navController.popBackStack()
            }

        } else {

            BackHandler() {
                manageMissBackDataLostAlertDialogStateInNoteScreen = true
            }
        }
    }


    //it show data lost alert dialog
    if (manageMissBackDataLostAlertDialogStateInNoteScreen) {
        manageMissBackDataLostAlertDialogStateInNoteScreen = handleMissBackDataLostAlertDialog(navController, dataViewModel, title)
    }


}


/*
this composable function handle the data lost when the
user by mistake try to exit the screen without saving
data then it prevent data lost.
*/
@Composable
fun handleMissBackDataLostAlertDialog(
    navController: NavController,
    dataViewModel: DataViewModel,
    title: String?,
): Boolean {
    var openCloseDataLostAlertDialogState by remember { mutableStateOf(true) }


    if (openCloseDataLostAlertDialogState) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { openCloseDataLostAlertDialogState = false },
            text = {

                if (title == "") {
                    Text(
                        modifier = Modifier.padding(0.dp),
                        text = "All data written in this session\nwill be lost."
                    )
                } else {
                    Text(
                        modifier = Modifier.padding(0.dp),
                        text = "All data Currently written in this\nsession will be lost."
                    )
                }

            },
            title = {
                Text(
                    modifier = Modifier.padding(0.dp),
                    text = "Do you want to exit?",
                    fontSize = 20.sp
                )
            },
            confirmButton = {
                TextButton(
                    modifier = Modifier.fillMaxWidth(.4f),
                    contentPadding = ButtonDefaults.TextButtonContentPadding,
                    colors = ButtonDefaults.textButtonColors(Color(0xFFC81E1E)),
                    onClick = {
                        dataViewModel.vibration()
                        openCloseDataLostAlertDialogState = false
                        navController.popBackStack()
                    }
                ) {
                    Text(text = "Yes", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    modifier = Modifier.fillMaxWidth(.4f),
                    colors = ButtonDefaults.textButtonColors(Color(0xFFA4A4A4)),
                    onClick = {
                        dataViewModel.vibration()
                        openCloseDataLostAlertDialogState = false
                    }
                ) {
                    Text(text = "  No  ", color = Color.White)
                }
            },
        )
    }
    return openCloseDataLostAlertDialogState
}


@SuppressLint("CoroutineCreationDuringComposition")
//share the saved data to other screens
@Composable
fun ShareDataToOtherApps(
    dataViewModel: DataViewModel,
    title: String?,
    description: String?,
    context: Context,
    interactionSource: MutableInteractionSource,
) {
    val coroutineScope = rememberCoroutineScope()

    var isClickable by remember { mutableStateOf(true) }

    /*
    (if) condition run when data share through main screen
    (else) condition run when data share through note screen
    */
    val shareData =
        if (dataViewModel.useDeleteIconOnHomeScreeState.observeAsState().value == true) {
            dataViewModel.itemDataForShareData.observeAsState().value
        } else {
            "$title\n\n$description"
        }


    Box(
        modifier = Modifier.fillMaxWidth(.6f), contentAlignment = Alignment.CenterEnd
    ) {
        Image(
            painter = painterResource(R.drawable.share),
            contentDescription = "share icon",
            modifier = Modifier
                .size(26.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = isClickable
                ) {
                    dataViewModel.vibration()
                    isClickable = false

                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, shareData)
                    context.startActivity(Intent.createChooser(intent, "Share data"))

                    coroutineScope.launch {
                        delay(1000)
                        isClickable = true
                    }

                }
        )

    }
}
