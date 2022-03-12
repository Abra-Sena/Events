package com.emissa.apps.events

import android.app.Application
import com.emissa.apps.events.model.EventDatabase

class EventsApplication : Application() {
    val database: EventDatabase by lazy {
        EventDatabase.getDatabase(this)
    }
}