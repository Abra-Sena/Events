package com.emissa.apps.events.views

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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EventFragment : Fragment(), EventClickListener {

    private val viewModel: EventsViewModel by activityViewModels {
        EventsViewModelFactory(
            (activity?.application as EventsApplication).database.eventDao()
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
//        viewModel.allEvents.observe(this.viewLifecycleOwner) { events ->
//            events.let { eventAdapter.submitList(it) }
//        }
        // save event
        val event = hashMapOf(
            "title" to "First Event",
            "category" to "Testing event creation, adding data to firestore",
            "date" to "03/11/2022"
        )

        // open second fragment to create new event
        binding.fabAddEvent.setOnClickListener {
//            findNavController().navigate(R.id.action_events_fragment_to_create_events)
            fragmentNavigation(
                supportFragmentManager = requireActivity().supportFragmentManager,
                CreateEventFragment()
            )
        }
    }

    override fun onEventClicked(event: Event) {
//        findNavController().navigate(R.id.action_events_fragment_to_event_details)
        fragmentNavigation(
            supportFragmentManager = requireActivity().supportFragmentManager,
            EventDetailsFragment()
        )
    }
}