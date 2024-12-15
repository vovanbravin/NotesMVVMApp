package com.example.notes.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface Dao {
    @Query("SELECT * FROM notes")
    fun getAllNotes(): LiveData<List<Note>>
    @Insert
    fun addNote(note: Note)
    @Query("DELETE FROM notes WHERE noteId = :id")
    fun deleteNote(id: Int)
    @Query("UPDATE notes SET Time = :time, Date = :date, Topic = :topic, Text = :text WHERE noteId = :id")
    fun updateNote(id: Int, time: String, date: String, topic: String, text: String)
}