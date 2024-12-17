package com.example.notes

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notes.db.Note
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

sealed class Screens(val route: String) {
    object Notes : Screens("Notes")
    object NewNote : Screens("Note")
    object EditNote : Screens("EditNote")
    object Settings : Screens("Settings")

}





@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notes(notes: List<Note>, theme: Theme, navController: NavController, viewModel: NoteViewModel) {
    val textColor = if (theme.color != Color.White) Color.White else Color.Black
    var expanded by mutableStateOf(false)
    var  selectedMode by mutableStateOf(0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.notes),
                            fontSize = (theme.fontSize + 5).sp
                        )
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = {expanded = true }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "Меню")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .padding(10.dp)) {
                            Text(text = "За дань",Modifier
                                .clickable {
                                    selectedMode = 1
                                })
                            Text(text = "За неделю", Modifier
                                .clickable {
                                    selectedMode = 2
                                })
                            Text(text = "За месяц", Modifier
                                .clickable {
                                    selectedMode = 3
                                })
                            Text(text = "За год", Modifier
                                .clickable {
                                    selectedMode = 4
                                })
                            Text(text = "За все время",Modifier
                                .clickable {
                                    selectedMode = 0
                                })
                        }
                    }
                    IconButton(onClick = { navController.navigate(Screens.Settings.route) }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Настройки")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = theme.color,
                    titleContentColor = if (theme.color != Color.White) Color.White else Color.Black,
                    actionIconContentColor = if (theme.color != Color.White) Color.White else Color.Black,
                )

            )
        },
        containerColor = theme.color,
        floatingActionButton =
        {
            FloatingActionButton(onClick = { navController.navigate(Screens.NewNote.route) }) {
                Icon(Icons.Filled.Create, contentDescription = "Добать заметку")
            }
        }

    ) {
        LazyColumn(
            Modifier
                .padding(it)
        ) {
            items(notes) { note ->
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 5.dp)
                        .background(
                            if (theme.color == Color.White) colorResource(R.color.notesForLightTheme)
                            else if (theme.color == Color.DarkGray) colorResource(R.color.notesForDarkGrayTheme)
                            else colorResource(R.color.notesForDarkTheme),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(5.dp)
                        .clickable {
                            navController.navigate("EditNote/${note.noteId}")
                        }
                ) {
                    Text(
                        text = note.topic, fontSize = (theme.fontSize + 2).sp,
                        modifier = Modifier
                            .fillMaxWidth(),
                        maxLines = 1,
                        color = textColor
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Text(text = note.date, fontSize = (theme.fontSize - 2).sp,
                            color = textColor)
                        Text(
                            text = note.text.substringBefore(" "),
                            fontSize = (theme.fontSize - 2).sp,
                            modifier = Modifier
                                .padding(start = 10.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = textColor
                        )
                        IconButton(onClick = { viewModel.deleteNote(note.noteId!!) },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = textColor
                            )) {
                            Icon(Icons.Filled.Delete, contentDescription = "Удалить",)
                        }

                    }
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNote(navController: NavController, theme: Theme, viewModel: NoteViewModel) {
    viewModel.clear()
    val textColor = if (theme.color != Color.White) Color.White else Color.Black
    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                },
                navigationIcon = {
                    Row(
                        modifier = Modifier
                            .clickable {
                                navController.navigate(Screens.Notes.route)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "Назад")
                        Text(
                            text = stringResource(id = R.string.notes),
                            fontSize = (theme.fontSize + 5).sp
                        )
                    }
                },
                actions = {
                    Text(
                        text = "Готово", fontSize = 20.sp,
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                                val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
                                viewModel.changeTime(
                                    LocalTime
                                        .now()
                                        .format(formatterTime)
                                )
                                val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                viewModel.changeDate(
                                    LocalDate
                                        .now()
                                        .format(formatterDate)
                                )
                                viewModel.addNote()
                                navController.navigate(Screens.Notes.route)
                            }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = theme.color,
                    titleContentColor = textColor,
                    actionIconContentColor = textColor,
                    navigationIconContentColor = textColor

                )
            )
        },
        containerColor = theme.color,
        contentColor = textColor
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TextField(
                value = viewModel.topic, onValueChange = { viewModel.changeTopic(it) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = theme.color,
                    unfocusedTextColor = textColor,
                    focusedTextColor = textColor,
                    unfocusedContainerColor = theme.color,
                    focusedIndicatorColor = theme.color,
                    unfocusedIndicatorColor = theme.color,
                    focusedLabelColor = textColor,
                    unfocusedLabelColor = textColor
                ),
                label = { Text(text = "Topic") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .border(
                        1.dp,
                        textColor,
                        RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                    ),
                singleLine = true,
                maxLines = 1
            )
            TextField(
                value = viewModel.text, onValueChange = { viewModel.changeText(it) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = theme.color,
                    unfocusedTextColor = textColor,
                    focusedTextColor = textColor,
                    unfocusedContainerColor = theme.color,
                    focusedIndicatorColor = theme.color,
                    unfocusedIndicatorColor = theme.color,
                    focusedLabelColor = textColor,
                    unfocusedLabelColor = textColor
                ),
                label = { Text(text = "Text") },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                    .border(
                        1.dp,
                        textColor,
                        RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                    )
            )

        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNote(
    notes: List<Note>,
    viewModel: NoteViewModel,
    noteId: Int,
    theme: Theme,
    navController: NavController,
) {
    val selectedId = notes.indexOfFirst { it.noteId == noteId }
    val selectedNote = notes[selectedId]
    val textColor = if (theme.color != Color.White) Color.White else Color.Black
    viewModel.changeTopic(selectedNote.topic)
    viewModel.changeText(selectedNote.text)
    viewModel.changeTime(selectedNote.time)
    viewModel.changeDate(selectedNote.date)
    Scaffold(
        topBar =
        {
            TopAppBar(
                title = {

                },
                navigationIcon =
                {
                    Row(
                        modifier = Modifier
                            .clickable {
                                navController.navigate(Screens.Notes.route)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "Назад")
                        Text(
                            text = stringResource(id = R.string.notes),
                            fontSize = (theme.fontSize + 5).sp
                        )
                    }
                },
                actions =
                {
                    Text(
                        text = "Готово", fontSize = 20.sp,
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                                navController.navigate(Screens.Notes.route)
                                viewModel.updateNote(noteId)
                            }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = theme.color,
                    titleContentColor = textColor,
                    actionIconContentColor = textColor,
                    navigationIconContentColor = textColor
                )


            )
        },
        containerColor = theme.color,
        contentColor = textColor


    )
    {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TextField(
                value = viewModel.topic, onValueChange = { viewModel.changeTopic(it) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = theme.color,
                    unfocusedTextColor = textColor,
                    focusedTextColor = textColor,
                    unfocusedContainerColor = theme.color,
                    focusedIndicatorColor = theme.color,
                    unfocusedIndicatorColor = theme.color,
                    focusedLabelColor = textColor,
                    unfocusedLabelColor = textColor
                ),
                label = { Text(text = "Topic") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .border(
                        1.dp,
                        textColor,
                        RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                    ),
                singleLine = true,
                maxLines = 1
            )
            TextField(
                value = viewModel.text, onValueChange = { viewModel.changeText(it) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = theme.color,
                    unfocusedTextColor = textColor,
                    focusedTextColor = textColor,
                    unfocusedContainerColor = theme.color,
                    focusedIndicatorColor = theme.color,
                    unfocusedIndicatorColor = theme.color,
                    focusedLabelColor = textColor,
                    unfocusedLabelColor = textColor
                ),
                label = { Text(text = "Text") },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                    .border(
                        1.dp,
                        textColor,
                        RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                    )
            )

        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    themeState: Theme,
    changeThemeState: (Theme) -> Unit,
    navController: NavController,
    themes: List<Theme>,
) {
    val textColor = if (themeState.color != Color.White) Color.White else Color.Black
    Scaffold(
        topBar =
        {
            TopAppBar(
                title = {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = "Настройки", fontSize = 20.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = themeState.color,
                    titleContentColor = textColor,
                    actionIconContentColor = textColor,
                    navigationIconContentColor = textColor

                )

            )
        },
        containerColor = themeState.color,
        floatingActionButton = {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
            {
                Button(onClick = { navController.navigate(Screens.Notes.route) }) {
                    Text(text = "Сохранить")
                }
            }
        }


    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
        )
        {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp)
            ) {
                Text(
                    text = "Тема: ", fontSize = themeState.fontSize.sp,
                    color = textColor
                )
                LazyColumn {
                    items(themes) { theme ->
                        Row(
                            Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = theme == themeState,
                                onClick = { changeThemeState(theme) })
                            Text(text = theme.name, fontSize = theme.fontSize.sp, color = textColor)
                        }
                    }
                }
            }
        }

    }
}




