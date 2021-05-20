package com.emkaydauda.contentprovidersample.viewmodels

import android.content.ContentResolver
import android.content.ContentValues
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emkaydauda.contentprovidersample.models.User
import com.emkaydauda.contentprovidersample.providers.FirestoreProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val contentResolver: ContentResolver): ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    private val randomUsers = listOf(
        User("Joe", "Biden", "joe@us.com", true),
        User("Bill", "Biden", "bill@us.com", true),
        User("Sam", "Biden", "sam@us.com", false),
        User("Jane", "Biden", "jane@us.com", true),
    )

    init {
        getUsers()
    }

    fun getUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            val usersCursor =  contentResolver.query(
                FirestoreProvider.USERS_URI,
                null,
                null,
                null,
                null
            )

            val users = mutableListOf<User>()

            while (usersCursor?.moveToNext() == true){
                val pUser = User(
                    usersCursor.getString(0),
                    usersCursor.getString(1),
                    usersCursor.getString(2),
                    usersCursor.getString(3).toBoolean()
                )

                users.add(pUser)
            }
            usersCursor?.close()
        }
    }

    fun addUser() {
        CoroutineScope(Dispatchers.IO).launch {
            val user = randomUsers.random()
            val contentValues = ContentValues().apply {
                this.put("firstName", user.firstName)
                this.put("lastName", user.lastName)
                this.put("email", user.email)
                this.put("verified", user.verified)
            }
            val a = contentResolver.insert(
                FirestoreProvider.USERS_URI,
                contentValues
            )

            if(a != null) {
                val currentUsers = _users.value ?: listOf()
                _users.postValue(currentUsers + user)
            }
        }
    }
}