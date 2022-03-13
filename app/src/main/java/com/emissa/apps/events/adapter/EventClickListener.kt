package com.emissa.apps.events.adapter

import com.emissa.apps.events.model.Event

interface EventClickListener {
    fun onEventClicked(event: Event)
    fun onEventLongClicked(event: Event)
}