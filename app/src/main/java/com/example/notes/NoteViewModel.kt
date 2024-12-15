package com.example.notes

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.notes.db.Note
import com.example.notes.db.NoteDatabase
import com.example.notes.db.NoteRepository

class NoteViewModel(application: Application): ViewModel(){
    val notesList: LiveData<List<Note>>
    private val repository: NoteRepository
    var time by mutableStateOf("")
    var date by mutableStateOf("")
    var topic by mutableStateOf("")
    var text by mutableStateOf("")

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

    fun updateNote(id: Int)
    {
        repository.updateNote(id, time, date, topic, text)
    }


    fun changeTopic(topic: String)
    {
        this.topic = topic
    }

    fun changeText(text: String)
    {
        this.text = text
    }

    fun changeTime(time: String)
    {
        this.time = time
    }

    fun changeDate(date: String)
    {
        this.date = date
    }

    fun clear()
    {
        this.time = ""
        this.date = ""
        this.topic = ""
        this.text = ""
    }






}