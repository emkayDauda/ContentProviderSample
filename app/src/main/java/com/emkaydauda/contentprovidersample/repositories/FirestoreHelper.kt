package com.emkaydauda.contentprovidersample.repositories

import android.util.Log
import com.emkaydauda.contentprovidersample.models.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class FirestoreHelper {
    companion object {
        val TAG = "FIRESTORE_HELPER"
    }

    private val firestoreDb = Firebase.firestore

    suspend fun saveUser(user: User): User? {
        return try {
            firestoreDb.collection("users").add(user).await()
            user
        } catch (e: Exception){
            Log.e(TAG, "${e.message}")
            return null
        }
    }

    suspend fun getUsers(): List<User>? {
        return try {
            val data = firestoreDb.collection("users")
                .get().await()

            val users = mutableListOf<User>()

            data.documents.forEach {
                val pUser = it.toObject(User::class.java)
                if (pUser != null) {
                    users.add(pUser)
                }
            }

            users
        } catch (e: Exception){
            Log.e(TAG, "${e.message}")

            return null
        }


    }
    
}