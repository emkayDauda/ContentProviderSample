package com.emkaydauda.contentprovidersample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.emkaydauda.contentprovidersample.models.User
import com.emkaydauda.contentprovidersample.providers.FirestoreProvider
import com.emkaydauda.contentprovidersample.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val viewmodel = MainViewModel(contentResolver)

        val usersCount = findViewById<TextView>(R.id.usersCount)
        val addButton = findViewById<Button>(R.id.addButton)

        viewmodel.users.observe(this, Observer {
            usersCount.text = "You have added ${it.size} users now."
        })

        addButton.setOnClickListener {
            viewmodel.addUser()
        }

    }
}