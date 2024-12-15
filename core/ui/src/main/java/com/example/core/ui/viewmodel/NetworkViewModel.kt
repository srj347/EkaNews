package com.example.core.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.core.ui.utils.NetworkChangeReceiver

class NetworkViewModel : ViewModel() {
    private val networkChangeReceiver = NetworkChangeReceiver()

    val isConnected: LiveData<Boolean> get() = networkChangeReceiver.isConnected

    fun registerReceiver(context: Context) {
        networkChangeReceiver.register(context)
    }

    fun unregisterReceiver(context: Context) {
        networkChangeReceiver.unregister(context)
    }
}