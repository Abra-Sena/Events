package com.emissa.apps.events.views

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emissa.apps.events.*
import com.emissa.apps.events.adapter.EventAdapter
import com.emissa.apps.events.adapter.EventClickListener
import com.emissa.apps.events.databinding.FragmentEventBinding
import com.emissa.apps.events.model.Event

class EventFragment : Fragment(), EventClickListener {

    private val viewModel: EventsViewModel by activityViewModels {
        EventsViewModelFactory(
            (requireActivity()?.application as EventsApplication).database.eventDao()
        )
    }
    private var mBinding: FragmentEventBinding? = null
    private val binding get() = mBinding!!
//    private val binding by lazy { FragmentEventBinding.inflate(layoutInflater) }
    private val eventAdapter by lazy { EventAdapter( this) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEventBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.eventRecyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = eventAdapter
        }
        // Attach an observer to the list of events to automatically update UI
        viewModel.allEvents.observe(this.viewLifecycleOwner) { events ->
            events.let { eventAdapter.updateEventsList(it) }
        }

        // open second fragment to create new event
        binding.fabAddEvent.setOnClickListener {
            val action = EventFragmentDirections.actionEventsFragmentToCreateEvents()
            this.findNavController().navigate(action)
        }
    }

    override fun onEventClicked(event: Event) {
        val action = EventFragmentDirections.actionEventsFragmentToEventDetails(event.id)
        this.findNavController().navigate(action)
    }

    override fun onEventLongClicked(event: Event) {
        Log.d("Events", "Inside event delete click listener")
        viewModel.deleteEvent(event)
        findNavController().navigateUp()
    }


    private fun deleteEvent() {

    }
}