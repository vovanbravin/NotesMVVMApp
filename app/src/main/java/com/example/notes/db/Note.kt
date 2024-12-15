package com.example.notes.db

import androidx.compose.runtime.Composable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
class Note {
    @PrimaryKey(autoGenerate = true)
    var noteId: Int? = null
    @ColumnInfo(name = "Time")
    var time: String = "00:00"
    @ColumnInfo(name = "Date")
    var date: String = "0000-00-00"
    @ColumnInfo(name = "Topic")
    var topic: String = ""
    @ColumnInfo(name = "Text")
    var text: String = ""

    constructor() {}

    constructor(time: String, date: String, topic: String, text: String)
    {
        this.time = time
        this.date = date
        this.topic = topic
        this.text = text
    }

    constructor(id:Int ,time: String, date: String, topic: String, text: String)
    {
        this.time = time
        this.date = date
        this.topic = topic
        this.text = text
        this.noteId = id
    }



}