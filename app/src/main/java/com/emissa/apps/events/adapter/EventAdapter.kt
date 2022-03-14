package com.emissa.apps.events.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.emissa.apps.events.R
import com.emissa.apps.events.fragmentNavigation
import com.emissa.apps.events.model.Event
import com.emissa.apps.events.views.CreateEventFragment

class EventAdapter(
    private val eventClickListener: EventClickListener,
    private var eventList: MutableList<Event> = mutableListOf()
): RecyclerView.Adapter<EventViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val eventView = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_item, parent, false)
        return EventViewHolder(eventView, eventClickListener)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]

        // this click is already handled under the bind method in the view holder

//        holder.itemView.setOnClickListener {
//            val activity = it.context as AppCompatActivity
//            fragmentNavigation(
//                supportFragmentManager = activity.supportFragmentManager,
//                CreateEventFragment()
//            )
//        }

        holder.bind(event)
    }

    override fun getItemCount(): Int = eventList.size

    // this updates the event list
    fun updateEventData(event: Event) {
        // add the new event at the top of the list
        eventList.add(0, event)
        // notifies the adapter that a new event was inserted
        notifyItemInserted(eventList.indexOf(event))
    }

    fun updateEventsList(events: List<Event>) {
        eventList = events.toMutableList()
        notifyDataSetChanged()
    }
}

class EventViewHolder(
    itemView : View,
    private val eventClicked: EventClickListener
) : RecyclerView.ViewHolder(itemView) {
    private val title: TextView = itemView.findViewById(R.id.event_title)
    private val category: TextView = itemView.findViewById(R.id.event_category)
    private val date: TextView = itemView.findViewById(R.id.event_date)

    fun bind(event: Event) {
        title.text = event.title
        category.text = event.category
        date.text = event.date

        itemView.setOnClickListener {
            eventClicked.onEventClicked(event)
        }
    }
}
