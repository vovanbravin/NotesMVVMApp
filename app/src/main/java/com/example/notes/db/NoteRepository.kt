package com.example.notes.db

import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteRepository(private val dao: Dao) {

    val notes = dao.getAllNotes()
    val scope = CoroutineScope(Dispatchers.Main)

    fun addNote(note: Note)
    {
        scope.launch(Dispatchers.IO) {
            dao.addNote(note)
        }
    }

    fun deleteNote(id: Int)
    {
        scope.launch(Dispatchers.IO) {
            dao.deleteNote(id)
        }
    }

    fun updateNote(id: Int, time: String, date: String, topic: String, text: String)
    {
        scope.launch(Dispatchers.IO) {
            dao.updateNote(id, time, date, topic, text)
        }
    }






}