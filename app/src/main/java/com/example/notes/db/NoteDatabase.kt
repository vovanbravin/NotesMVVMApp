package com.example.notes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase: RoomDatabase(){

    abstract fun getDao(): Dao

    companion object
    {
        fun getDb(context: Context) : NoteDatabase
        {
            return Room.databaseBuilder(context.applicationContext, NoteDatabase::class.java,"notes").build()
        }

    }
}