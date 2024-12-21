package com.example.forget_hub.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.forget_hub.R
import com.example.forget_hub.viewModel.DataViewModel
import com.example.forget_hub.roomDatabase.ForgetHubData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
// this composable function take data from database and rendered vertical staggered grid
fun GridListShow(
    dataViewModel: DataViewModel,
    navController: NavController,
    backgroundColor: Color,
    interactionSource: MutableInteractionSource,
) {
    // it manage scroll state of vertical staggered grid when navigate one screen to another
    val gridState = remember { dataViewModel.scrollState }

    // observer the data from database when data save and update
    val data by dataViewModel.getData.observeAsState(emptyList())


    // it show the grid in home screen
    LazyVerticalStaggeredGrid(
        modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 15.dp, top = 10.dp),
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy((6).dp),
        verticalItemSpacing = (6).dp,
        flingBehavior = ScrollableDefaults.flingBehavior(),
        state = gridState,
        userScrollEnabled = !dataViewModel.useOpenCloseNavigationDrawerState.observeAsState().value!!
    ) {
        items(data.reversed(), key = { it.id }) {
            //calling item box which is rendered in home screen
            GridListBox(
                it.id,
                it.title,
                it.description,
                it.date,
                navController,
                backgroundColor,
                dataViewModel,
                interactionSource,
            )
        }
    }

    // it manage scroll state of vertical staggered grid when navigate one screen to another
    DisposableEffect(Unit) {
        onDispose {
            dataViewModel.updateScrollState(gridState)
        }
    }
}


@SuppressLint("ServiceCast")
@Stable
@Composable
// this composable function manage all home screen UI which is rendered on home screen
fun MainScreen(
    navController: NavController,
    dataViewModel: DataViewModel,
    context: Context,
    interactionSource: MutableInteractionSource,
) {
    var manageButtonOpenCloseState by remember { mutableStateOf(true) }
    val backgroundColor = dataViewModel.backgroundColor.observeAsState().value
    val coroutineScope = rememberCoroutineScope()

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
                    .background(backgroundColor!!)
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
                    modifier = Modifier.fillMaxWidth(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(R.drawable.menu_),
                        contentDescription = "menuIcon",
                        modifier = Modifier
                            .size(35.dp)
                            .clickable(
                                enabled = manageButtonOpenCloseState,
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                coroutineScope.launch {
                                    dataViewModel.vibration()
                                    dataViewModel.updateEnableDisableDeleteIconOnHomeScreenState(false)
                                    dataViewModel.updateOpenCloseNavigationDrawerState(true)
                                }
                            }
                    )
                    Text(
                        text = "ForgetHub",
                        fontSize = 25.sp,
                        color = Color.Black,
                        fontWeight = FontWeight(300),
                        modifier = Modifier.padding(start = 40.dp)
                    )

                    // it enable disable share and delete icon on home screen in header
                    if (dataViewModel.useDeleteIconOnHomeScreeState.observeAsState().value == true) {

                        //disable or close share and delete icon when trigger system back
                        BackHandler {
                            dataViewModel.updateEnableDisableDeleteIconOnHomeScreenState(false)
                        }

                        //calling share icon UI
                        ShareDataToOtherApps(dataViewModel, "title", "description", context, interactionSource,)

                        /*
                        In this function calling (true) argument manage the
                        alert dialog on home screen, when the delete icon
                        click from the home screen then (dismiss alert
                        dialog and also dismiss delete icon on home screen)
                        */
                        //calling delete icon UI
                        DeleteIcon(dataViewModel.itemIdForDeleteItem.observeAsState().value, dataViewModel, navController, true, interactionSource, context)
                    }
                }
            }

            // show image on home screen if no item on home screen else show items in the form of grid
            if (dataViewModel.getData.observeAsState(emptyList()).value.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .background(Transparent)
                        .padding(top = 100.dp), contentAlignment = Alignment.TopCenter
                ) {
                    Image(
                        painter = painterResource(R.drawable.backimage),
                        contentDescription = "initial launch app background image",
                        modifier = Modifier
                            .size(width = 350.dp, 430.dp)
                            .background(Transparent)
                    )
                }
            } else {

                //calling a function which is rendered vertical staggered grid
                GridListShow(dataViewModel, navController, backgroundColor, interactionSource)
            }
        }

        //it show add data icon on bottom right corner in home screen
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
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(backgroundColor!!)
                    .size(50.dp)
                    .clickable(
                        enabled = manageButtonOpenCloseState && !dataViewModel.useOpenCloseNavigationDrawerState.observeAsState().value!!,
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        manageButtonOpenCloseState = false
                        dataViewModel.vibration()

                        coroutineScope.launch(Dispatchers.Main) {
                            dataViewModel.updateOpenCloseNavigationDrawerState(false)
                            navController.navigate("firstTimeDataSaveScreen") {}
                            delay(800)
                            manageButtonOpenCloseState = true
                        }

                    }
            ) {
                Image(
                    painter = painterResource(R.drawable.add),
                    contentDescription = "AddNotes",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }

    // it open the navigation drawer on home screen
    if (dataViewModel.useOpenCloseNavigationDrawerState.observeAsState().value == true) {
        NavigationDrawer(dataViewModel, interactionSource, context)
    }

}


@Stable
@Composable
// it show delete icon on home screen in header
fun DeleteIcon(
    deleteItemID: Int?,
    dataViewModel: DataViewModel,
    navController: NavController,
    manageAlertDialogInBothScreen: Boolean,
    interactionSource: MutableInteractionSource,
    context: Context,
) {
    val enableDeleteAlertDialog = remember { mutableStateOf(false) }

    val deleteIconAnimationEnableDisable = dataViewModel.useDeleteIconOnHomeScreeState.observeAsState().value


    //delete icon pendulum animation
    val infiniteTransition = rememberInfiniteTransition(label = "angle")
    val angle by infiniteTransition.animateFloat(
        initialValue = -13f,
        targetValue = 13f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 600,
                easing = LinearEasing
            ), repeatMode = RepeatMode.Reverse
        ), label = "rotationZ"
    )


    Box(
        modifier = Modifier
            .graphicsLayer {
                rotationZ = if (deleteIconAnimationEnableDisable == true) {
                    angle
                } else {
                    0f
                }
            }
            .fillMaxWidth(
                //manage delete icon position in Home screen and Note screen
                if (manageAlertDialogInBothScreen) {
                    .7f
                } else {
                    .4f
                }
            )
            , contentAlignment = Alignment.CenterEnd
    ) {

        Image(painter = painterResource(R.drawable.deleteicon),
            contentDescription = "Delete Icon",
            modifier = Modifier
                .size(30.dp)
                .clickable(interactionSource = interactionSource, indication = null) {
                    dataViewModel.vibration()
                    enableDeleteAlertDialog.value = true
                }
        )

    }

    //enable delete alert dialog when click on delete icon by calling deleteAlertDialog
    if (enableDeleteAlertDialog.value) {

        // the return value use to click again delete icon when click happen outside the alert dialog
        enableDeleteAlertDialog.value = deleteAlertDialog(
            navController,
            dataViewModel,
            deleteItemID!!,
            manageAlertDialogInBothScreen,
            context
        )
    }


}

@Stable
@Composable
//delete item alert dialog
fun deleteAlertDialog(
    navController: NavController,
    dataViewModel: DataViewModel,
    deleteItemID: Int,
    manageAlertDialogInBothScreen: Boolean,
    context: Context,
): Boolean {

    var deleteAlertDialogEnableDisable by remember { mutableStateOf(true) }

    if (deleteAlertDialogEnableDisable) {
        AlertDialog(
            tonalElevation = 10.dp,
            modifier = Modifier.padding(0.dp),
            containerColor = Color.White,
            onDismissRequest = { deleteAlertDialogEnableDisable = false },
            title = {
                Text(
                    modifier = Modifier.padding(0.dp),
                    text = "Do you want to delete?",
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = "The data permanently remove\nfrom this application.",
                    color = Color.Black,
                    fontSize = 15.sp
                )
            },

            confirmButton = {
                TextButton(
                    modifier = Modifier.fillMaxWidth(.4f),
                    contentPadding = ButtonDefaults.TextButtonContentPadding,
                    colors = ButtonDefaults.textButtonColors(Color(0xFFC81E1E)),
                    onClick = {
                        dataViewModel.vibration()
                        dataViewModel.delete(
                            ForgetHubData(
                                deleteItemID, "deleteTitle", "deleteDescription", "deleteDate"
                            )
                        )
                        Toast
                            .makeText(context, "Delete", Toast.LENGTH_SHORT)
                            .show()

                        /*
                        when the delete icon click from note screen then (else)
                        condition run and when the delete icon click from the
                        home screen by holding the item the (if) condition run.
                        */
                        if (manageAlertDialogInBothScreen) {
                            //dismiss alert dialog on home screen and also delete icon
                            deleteAlertDialogEnableDisable =
                                false // dismiss delete alert dialog from home screen
                            dataViewModel.updateEnableDisableDeleteIconOnHomeScreenState(false) // disable delete icon from home screen
                        } else {
                            //dismiss alert dialog in note screen and pop back to home screen
                            deleteAlertDialogEnableDisable = false
                            navController.popBackStack()
                        }

                    }) {
                    Text(text = "Delete", color = Color.White)
                }

            },

            dismissButton = {
                TextButton(
                    modifier = Modifier.fillMaxWidth(.4f),
                    colors = ButtonDefaults.textButtonColors(Color(0xFFA4A4A4)),
                    onClick = {
                        dataViewModel.vibration()
                        deleteAlertDialogEnableDisable = false

                        //disable delete icon when dismiss the delete alert dialog
                        dataViewModel.updateEnableDisableDeleteIconOnHomeScreenState(false)
                    }
                ) {
                    Text(text = "Cancel", color = Color.White)
                }

            }

        )

    }

    return deleteAlertDialogEnableDisable

}


@Stable
@OptIn(ExperimentalFoundationApi::class)
@Composable
//this composable function rendered each items
fun GridListBox(
    id: Int,
    title: String,
    description: String,
    date: String,
    navController: NavController,
    backgroundColor: Color,
    dataViewModel: DataViewModel,
    interactionSource: MutableInteractionSource,
) {
    val coroutineScope = rememberCoroutineScope()
    val manageContinuouslyMultipleClicks = remember { mutableStateOf(true) }

    // animation of box when long click happened
    val boxScaleAnimationState = remember { mutableStateOf(false) }
    val animatedScale by animateFloatAsState(
        targetValue = if (boxScaleAnimationState.value) {
            1.1f
        } else {
            0.9f
        },
        label = "scale",
    )


    //it is use when the item box long click then change item border color and width
    var deleteItemBorderWidth = 1.dp
    var deleteItemBorderColor = Color(0xCC717171)
    if (dataViewModel.useDeleteIconOnHomeScreeState.observeAsState().value == true) {
        if (id == dataViewModel.itemIdForDeleteItem.value) {
            deleteItemBorderWidth = 2.5.dp
            deleteItemBorderColor = backgroundColor
        }
    }



    Box(
        modifier = Modifier
            .then(
                if (boxScaleAnimationState.value) {
                    Modifier.graphicsLayer(
                        scaleX = animatedScale,
                        scaleY = animatedScale,

                        )
                } else {
                    Modifier
                }
            )
            .fillMaxWidth(.48f)
            .clip(
                RoundedCornerShape(15.dp),
            )
            .combinedClickable(
                enabled = !dataViewModel.useOpenCloseNavigationDrawerState.observeAsState().value!!,
                interactionSource = interactionSource, indication = null,
                onClick = {
                    if (manageContinuouslyMultipleClicks.value) {
                        manageContinuouslyMultipleClicks.value = false
                        coroutineScope.launch {
                            dataViewModel.vibration()
                            dataViewModel.updateEnableDisableDeleteIconOnHomeScreenState(false)
                            dataViewModel.updateOpenCloseNavigationDrawerState(false)
                            navController.navigate(route = "preSaveDataEditScreen/$id/$title/$description")
                            delay(700)
                            manageContinuouslyMultipleClicks.value = true

                        }
                    }

                },
                onLongClick = {
                    coroutineScope.launch {
                        boxScaleAnimationState.value = true
                        dataViewModel.vibration()

                        //disable long click interaction while opening navigation drawer
                        dataViewModel.updateOpenCloseNavigationDrawerState(false)

                        //delay for make real box scale animation
                        delay(100)
                        boxScaleAnimationState.value = false

                        dataViewModel.updateEnableDisableDeleteIconOnHomeScreenState(true)

                        //it is use for share data and delete item in specific condition
                        dataViewModel.updateShareDataAndDeleteItemId(id, "$title\n\n$description")
                    }
                },

                )

            .border(deleteItemBorderWidth, deleteItemBorderColor, RoundedCornerShape(15.dp)),


        ) {


        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(1f)


        ) {
            Text(
                text = title,
                fontSize = 20.sp,
            )


            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xCC717171),
                maxLines = 8,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 10.dp)
            )

            Text(
                text = date,
                fontSize = 10.sp,
                color = Color(0xCC717171),
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
// navigation drawer
fun NavigationDrawer(
    dataViewModel: DataViewModel,
    interactionSource: MutableInteractionSource,
    context: Context,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val scope = rememberCoroutineScope()

    //navigation drawer close when system back is happen
    BackHandler {
        dataViewModel.updateOpenCloseNavigationDrawerState(false)
    }


    //navigation drawer
    ModalNavigationDrawer(
        scrimColor = Transparent,
        modifier = Modifier
            .fillMaxWidth(0.7f),
        gesturesEnabled = false,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.background(Transparent),
                drawerContainerColor = Color.White
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 22.dp)
                        .fillMaxHeight(1f)
                ) {
                    Column(

                    ) {
                        Row(
                            modifier = Modifier.padding(start = 15.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(35.dp)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    )
                                    {
                                        scope.launch {
                                            dataViewModel.vibration()

                                            /*
                                            it change the navigation drawer calling state when the
                                            navigation drawer calling from main screen function
                                            so that we can open navigation drawer again and again
                                            by clicking menu icon
                                            */
                                            dataViewModel.updateOpenCloseNavigationDrawerState(false)

                                            //it close the navigation drawer by clicking close menu icon
                                            drawerState.close()
                                        }
                                    },
                                painter = painterResource(R.drawable.openmenu),
                                contentDescription = "menu back icon"
                            )


                            Text(
                                text = "ForgetHub",
                                fontSize = 25.sp,
                                color = Color.Black,
                                fontWeight = FontWeight(300),
                                modifier = Modifier.padding(start = 40.dp)
                            )

                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(top = 7.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp),
                        ) {
                            Row(
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        dataViewModel.vibration()
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSf-M_HrIPLOEIcm_WYetNIxM_vC6wig1Ze20NVoQWl13Da3dQ/viewform?usp=header")
                                        )
                                        context.startActivity(intent)
                                    }
                                    .padding(start = 35.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    modifier = Modifier.size(30.dp),
                                    painter = painterResource(R.drawable.bug),
                                    contentDescription = "bug icon"
                                )

                                Text(
                                    text = "Report Bug",
                                    fontSize = 17.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight(300),
                                )

                            }
                        }
                    }
                }
            }
        }
    ) {

    }
}


