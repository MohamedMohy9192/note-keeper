package com.androideradev.www.notekeeper

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class NoteGetTogetherHelper(context: Context, lifecycle: Lifecycle) : LifecycleObserver {

    init {
        lifecycle.addObserver(this)
    }

    var latitude = 0.0
    var longitude = 0.0
    val tag = NoteGetTogetherHelper::class.simpleName

    private val pseudoLocationManager = PseudoLocationManager(context) { latitude, longitude ->
        this.latitude = latitude
        this.longitude = longitude

        Log.i(tag, "${this.latitude}, ${this.longitude}")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startEventHandler() {
        pseudoLocationManager.start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopEventHandle() {
        pseudoLocationManager.stop()
    }
}