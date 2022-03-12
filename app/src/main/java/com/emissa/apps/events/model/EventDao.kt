package com.emissa.apps.events.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    /**
     * Returns all saved events, ordered by title
     */
    @Query("SELECT * from event ORDER BY title ASC")
    fun getAllEvents(): Flow<List<Event>>

    @Query("SELECT * from event WHERE id = :id")
    fun getEvent(id: Int): Flow<Event>

    @Insert
    suspend fun insertNewEvent(event: Event)
    @Update
    suspend fun updateEvent(event: Event)
    @Delete
    suspend fun deleteEvent(event: Event)
}