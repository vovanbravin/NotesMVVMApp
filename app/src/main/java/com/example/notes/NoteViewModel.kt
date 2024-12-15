package com.example.notes

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.notes.db.Note
import com.example.notes.db.NoteDatabase
import com.example.notes.db.NoteRepository

class NoteViewModel(application: Application): ViewModel(){
    val notesList: LiveData<List<Note>>
    private val repository: NoteRepository
    val time by mutableStateOf("")
    val date by mutableStateOf("")
    val topic by mutableStateOf("")
    val text by mutableStateOf("")

    init {
        val db = NoteDatabase.getDb(application)
        val dao = db.getDao()
        repository = NoteRepository(dao)
        notesList = repository.notes
    }

    fun addNote()
    {
        repository.addNote(Note(time,date,topic,text))
    }

    fun deleteNote(id: Int)
    {
        repository.deleteNote(id)
    }





}