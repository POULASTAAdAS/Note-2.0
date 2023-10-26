package com.example.data.repository

import com.example.model.Note
import com.example.model.User
import com.example.utils.UserExists
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.elemMatch
import org.litote.kmongo.eq
import org.litote.kmongo.pullByFilter

class NoteDataBaseOperationImpl(
    database: CoroutineDatabase
) : NoteDataBaseOperation {

    private val userCollection = database.getCollection<User>()


    override suspend fun createJWTAuthenticatedUser(user: User): UserExists {
        val tempUser = userCollection.findOne(filter = User::email eq user.email)

        return if (tempUser != null) {
            if (tempUser.password == user.password) UserExists.YES_SAME_PASSWORD
            else UserExists.YES_DIFF_PASSWORD
        } else {
            userCollection.insertOne(user)
            UserExists.NO
        }
    }
    override suspend fun createGoogleAuthenticatedUser(user: User): UserExists {
        val tempUser = userCollection.findOne(filter = User::name eq user.name)

        return if (tempUser != null) UserExists.YES_SAME_PASSWORD
        else {
            userCollection.insertOne(user)
            UserExists.NO
        }
    }


    override suspend fun getAllNoteForJWTAuthenticatedUser(email: String): List<Note> {
        val list: ArrayList<Note> = ArrayList()

        userCollection.findOne(filter = User::email eq email)?.listOfNote.run {
            if (this != null) list.addAll(this)
        }
        return list
    }
    override suspend fun getAllNoteForGoogleAuthenticatedUser(sub: String): List<Note> {
        val list: ArrayList<Note> = ArrayList()

        userCollection.findOne(filter = User::sub eq sub)?.listOfNote.run {
            if (this != null) list.addAll(this)
        }
        return list
    }


    override suspend fun addOneForJWTUser(note: Note, email: String): Boolean {
        val find = userCollection.find(
            Filters.eq("email", email),
            Filters.eq("listOfNote._id", note._id)
        ).first()

        if (find == null) {
            userCollection.updateOne(
                filter = Filters.eq("email", email),
                update = Updates.push(User::listOfNote.name, note)
            )
            return true
        }
        return false
    }
    override suspend fun addOneForGoogleUser(note: Note, sub: String): Boolean {
        val find = userCollection.find(
            Filters.eq("sub", sub),
            Filters.eq("listOfNote._id", note._id)
        ).first()

        if (find == null) {
            userCollection.updateOne(
                filter = Filters.eq("sub", sub),
                update = Updates.push(User::listOfNote.name, note)
            )
            return true
        }
        return false
    }


    override suspend fun addMultipleForJWTUser(listOfNote: List<Note>, email: String) {
        listOfNote.forEach {
            addOneForJWTUser(it, email)
        }
    }
    override suspend fun addMultipleForGoogleUser(listOfNote: List<Note>, sub: String) {
        listOfNote.forEach {
            addOneForGoogleUser(it, sub)
        }
    }


    override suspend fun updateOneForJWTUser(note: Note, email: String) {
        val find = and(
            Filters.eq("email", email),
            Filters.eq("listOfNote._id", note._id)
        )


        userCollection.updateOne(
            filter = find,
            update = Updates.set("listOfNote.$", note)
        )
    }
    override suspend fun updateOneForGoogleUser(note: Note, sub: String) {
        val find = and(
            Filters.eq("sub", sub),
            Filters.eq("listOfNote._id", note._id)
        )


        userCollection.updateOne(
            filter = find,
            update = Updates.set("listOfNote.$", note)
        )
    }


    override suspend fun updateMultipleForJWTUser(listOfNote: List<Note>, email: String) {
        listOfNote.forEach {
            updateOneForJWTUser(it, email)
        }
    }
    override suspend fun updateMultipleForGoogleUser(listOfNote: List<Note>, sub: String) {
        listOfNote.forEach {
            updateOneForGoogleUser(it, sub)
        }
    }


    override suspend fun deleteOneForJWTUser(_id: String, email: String) {
        val find = and(
            User::email eq email,
            User::listOfNote.elemMatch(Note::_id eq _id)
        )

        val update = pullByFilter(User::listOfNote, Note::_id eq _id)

        userCollection.updateOne(find, update)
    }
    override suspend fun deleteOneForGoogleUser(_id: String, sub: String) {
        val find = and(
            User::sub eq sub,
            User::listOfNote.elemMatch(Note::_id eq _id)
        )

        val update = pullByFilter(User::listOfNote, Note::_id eq _id)

        userCollection.updateOne(find, update)
    }

    override suspend fun deleteMultipleForJWTUser(listOf_id: List<String>, email: String) {
        listOf_id.forEach {
            deleteOneForJWTUser(it, email)
        }
    }
    override suspend fun deleteMultipleForGoogleUser(listOf_id: List<String>, sub: String) {
        listOf_id.forEach {
            deleteOneForGoogleUser(it, sub)
        }
    }

    override suspend fun deleteJWTUser(email: String) {
        userCollection.deleteOne(User::email eq email)
    }
    override suspend fun deleteGoogleUser(sub: String) {
        userCollection.deleteOne(User::sub eq sub)
    }
}