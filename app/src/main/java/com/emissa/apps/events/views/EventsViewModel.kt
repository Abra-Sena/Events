package com.emissa.apps.events

import androidx.lifecycle.*
import com.emissa.apps.events.model.Event
import com.emissa.apps.events.model.EventDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EventsViewModel(private val eventDao: EventDao) : ViewModel() {
//    val allEvents: Flow<List<Event>> = eventDao.getAllEvents()
    val allEvents: LiveData<List<Event>> = eventDao.getAllEvents().asLiveData()

    fun updateEvent(
        eventId: Int, eventTitle: String, eventCategory: String, eventDate: String
    ) {
        val eventToUpdate = getUpdatedEventDetails(eventId, eventTitle, eventCategory, eventDate)
        updateEvent(eventToUpdate)
    }
    private fun updateEvent(event: Event) {
        viewModelScope.launch { eventDao.updateEvent(event) }
    }

    fun addNewEvent(
        eventTitle: String,
        eventCategory: String,
        eventDate: String
    ) {
        val newEvent = getNewEventDetails(eventTitle, eventCategory, eventDate)
        insertEvent(newEvent)
    }
    private fun insertEvent(event: Event) {
        viewModelScope.launch { eventDao.insertNewEvent(event) }
    }

    fun retrieveEvent(id: Int): LiveData<Event> {
        return eventDao.getEvent(id).asLiveData()
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch { eventDao.deleteEvent(event) }
    }

    private fun getNewEventDetails(
        eventTitle: String, eventCategory: String, eventDate: String
    ): Event {
        return Event(
            title = eventTitle,
            category = eventCategory,
            date = eventDate
        )
    }
    private fun getUpdatedEventDetails(
        eventId: Int,
        eventTitle: String,
        eventCategory: String,
        eventDate: String
    ): Event {
        return Event(
            id = eventId,
            title = eventTitle,
            category = eventCategory,
            date = eventDate
        )
    }
}

/**
 * This factory class returns an instance of the view model above
 */
class EventsViewModelFactory(private val eventDao: EventDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventsViewModel(eventDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class.")
    }

}