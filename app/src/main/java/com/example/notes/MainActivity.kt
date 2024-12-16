package com.example.notes

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notes.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themes = listOf(Theme("Dark", Color.Black, 20),
                Theme("Light",Color.White, 20),
                Theme("DarkGray", Color.DarkGray, 20))
            var themeState by mutableStateOf(themes[1])
            fun cgangeThemeState(theme: Theme) {themeState = theme}
            val navController = rememberNavController()
            val viewModel = NoteViewModel(application = application)
            val notesList by viewModel.notesList.observeAsState(initial = listOf())
            NavHost(navController = navController, startDestination = Screens.Notes.route) {
                composable(Screens.Notes.route){ Notes(notes = notesList, theme = themeState, navController = navController, viewModel)}
                composable(Screens.NewNote.route){ NewNote(navController = navController, theme = themeState,viewModel = viewModel)}
                composable(Screens.EditNote.route + "/{noteId}",
                    arguments = listOf(navArgument("noteId"){type = NavType.IntType})) {
                    EditNote(notesList,viewModel, noteId = it.arguments!!.getInt("noteId"),themeState,navController )
                }
                composable(Screens.Settings.route){ Settings(themeState = themeState, changeThemeState = {cgangeThemeState(it)}, navController = navController, themes)}
            }
        }
    }
}

class Theme(val name: String, val color: Color, val fontSize: Int)

