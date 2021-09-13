package com.androideradev.www.notekeeper

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class NoteGetTogetherHelper(context: Context, private val lifecycle: Lifecycle) :
    LifecycleObserver {

    init {
        lifecycle.addObserver(this)
    }

    var latitude = 0.0
    var longitude = 0.0
    private val tag = NoteGetTogetherHelper::class.simpleName

    private val pseudoLocationManager = PseudoLocationManager(context) { latitude, longitude ->
        this.latitude = latitude
        this.longitude = longitude

        Log.i(tag, "${this.latitude}, ${this.longitude}")
    }

    private val pseudoMessagingManager = PseudoMessagingManager(context)
    private var pseudoMessagingConnection: PseudoMessagingConnection? = null

    fun sendMessage(note: NoteInfo) {
        val getTogetherMessage = "$latitude|$longitude|${note.title}|${note.course?.title}"
        pseudoMessagingConnection?.send(getTogetherMessage)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startEventHandler() {
        pseudoLocationManager.start()
        pseudoMessagingManager.connect { connection ->
            Log.d(tag, "Connection callback - Lifecycle state:${lifecycle.currentState}")
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                pseudoMessagingConnection = connection
            } else {
                connection.disconnect()
            }

        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopEventHandle() {
        pseudoLocationManager.stop()
        pseudoMessagingConnection?.disconnect()
    }
}