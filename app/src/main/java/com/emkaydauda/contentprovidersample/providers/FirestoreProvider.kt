package com.emkaydauda.contentprovidersample.providers

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import com.emkaydauda.contentprovidersample.models.User
import com.emkaydauda.contentprovidersample.repositories.FirestoreHelper
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.lang.IllegalArgumentException

class FirestoreProvider : ContentProvider() {

    companion object {
        const val TAG = "FIREBASE_PROVIDER"
        const val PROVIDER_AUTHORITY =
            "com.emkaydauda.contentprovidersample.providers.FirestoreProvider"
        const val URL = "content://${PROVIDER_AUTHORITY}"
        val USERS_URI = Uri.parse("${URL}/users")


        val uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            .apply {
                this.addURI(
                    PROVIDER_AUTHORITY,
                    "users",
                    1
                )
            }

        lateinit var firestoreHelper: FirestoreHelper
    }

    override fun onCreate(): Boolean {
        firestoreHelper = FirestoreHelper()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        when (uriMatcher.match(uri)) {
            1 -> {
                val usersCursor = MatrixCursor(
                    listOf(
                        "firstName",
                        "lastName",
                        "email",
                        "verified"
                    ).toTypedArray()
                )
                var users: List<User>

                runBlocking {
                    users = firestoreHelper.getUsers() ?: listOf()
                    users.forEach {
                        usersCursor.addRow(listOf(it.firstName, it.lastName, it.email, it.verified))
                    }
                }

                return usersCursor
            }

            else -> throw IllegalArgumentException()
        }
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when (uriMatcher.match(uri)) {
            1 -> {
                val user = User(
                    firstName = values?.getAsString("firstName"),
                    lastName = values?.getAsString("lastName"),
                    email = values?.getAsString("email"),
                    verified = values?.getAsBoolean("verified")
                )
                var _uri: Uri? = null
                runBlocking {
                    try {
                        val savedUser = firestoreHelper.saveUser(user)
                        _uri = Uri.withAppendedPath(USERS_URI, Uri.encode("${savedUser?.email}"))
                    } catch (e: Exception) {
                        Log.e(TAG, "${e.message}")
                    }
                }
                return _uri
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }
}