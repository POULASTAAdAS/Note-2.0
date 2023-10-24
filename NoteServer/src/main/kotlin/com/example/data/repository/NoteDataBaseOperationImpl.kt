package com.example.data.repository

import com.example.model.Note
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class NoteDataBaseOperationImpl(
    database: CoroutineDatabase
) : NoteDataBaseOperation {

    private val noteCollection = database.getCollection<Note>()

    override suspend fun getAll(): List<Note> = noteCollection.find().toList()

    override suspend fun addOne(note: Note): Boolean =
        noteCollection.insertOne(note).wasAcknowledged()

    override suspend fun addMultiple(listOfNote: List<Note>): Boolean =
        noteCollection.insertMany(listOfNote).wasAcknowledged()

    override suspend fun upDateOne(note: Note): Boolean =
        noteCollection.updateOne(
            filter = Note::_id eq note._id,
            target = note
        ).wasAcknowledged()

    override suspend fun updateMultiple(listOfNote: List<Note>) {
        listOfNote.forEach {
            noteCollection.updateOne(
                filter = Note::_id eq it._id,
                target = it
            )
        }
    }

    override suspend fun deleteOne(_id: String): Boolean =
        noteCollection.deleteOne(filter = Note::_id eq _id).wasAcknowledged()
}