package com.example.notes

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notes.ui.theme.NotesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.prefs.Preferences



val Context.dataStore by preferencesDataStore(name = "settings")
val IndexTheme = intPreferencesKey("index")


class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    @RequiresApi(Build.VERSION_CODES.O)
    private var savedIndex = 1
    @SuppressLint("UnrememberedMutableState")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themes = listOf(Theme("Dark", Color.Black, 20),
                Theme("Light",Color.White, 20),
                Theme("DarkGray", Color.DarkGray, 20))
            val indexTheme by this@MainActivity.dataStore.data.map { it[IndexTheme] ?: 1 }
                .collectAsState(initial = 0)
            savedIndex = indexTheme
            var themeState by mutableStateOf(themes[indexTheme])
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        super.onDestroy()
        CoroutineScope(Dispatchers.IO).launch {
            saveData(savedIndex)
        }
    }

    private suspend fun saveData(index: Int) {
        dataStore.edit { preferences ->
            preferences[IndexTheme] = index
        }
    }



}

class Theme(val name: String, val color: Color, val fontSize: Int)





