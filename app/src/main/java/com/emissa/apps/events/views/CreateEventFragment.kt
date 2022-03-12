package com.emissa.apps.events.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.emissa.apps.events.EventsApplication
import com.emissa.apps.events.EventsViewModel
import com.emissa.apps.events.EventsViewModelFactory
import com.emissa.apps.events.databinding.FragmentCreateEventBinding
import com.emissa.apps.events.fragmentNavigation
import com.emissa.apps.events.model.Event


class CreateEventFragment : Fragment() {
    private val LOG_TAG: String = "CreateEventFragment"
    private val navigationArgs: EventDetailsFragmentArgs by navArgs()
    lateinit var event: Event

    private val viewModel: EventsViewModel by activityViewModels {
        EventsViewModelFactory(
            (activity?.application as EventsApplication).database.eventDao()
        )
    }
    private var mBinding: FragmentCreateEventBinding? = null
    private val binding get() = mBinding!!

//    private val binding by lazy { FragmentCreateEventBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCreateEventBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarView.setOnDateChangeListener { calendarView, year, month, day ->
            Log.d(LOG_TAG, "Date is: $calendarView.date or $month/$day/$year")
        }

        val id = navigationArgs.eventId
        if (id > 0) {
            viewModel.retrieveEvent(id).observe(this.viewLifecycleOwner) { selectedEvent ->
                event = selectedEvent
                bindEvent(event)
            }
        } else {
            binding.btnAddEvent.setOnClickListener {
                Log.d(LOG_TAG, "Clicked on btn Done inside create event")
                addNewEvent()
                fragmentNavigation(
                    supportFragmentManager = requireActivity().supportFragmentManager,
                    EventFragment()
                )
            }
        }
    }

    private fun bindEvent(event: Event) {
//        val date
        binding.apply {
            etEventTitle.setText(event.title)
            etEventCategory.setText(event.category)
            cvEventDate.text = event.date
        }
    }
    private fun addNewEvent() {
        viewModel.addNewEvent(
            binding.etEventTitle.text.toString(),
            binding.etEventCategory.text.toString(),
            binding.calendarView.date.toString()
        )
        fragmentNavigation(
            supportFragmentManager = requireActivity().supportFragmentManager,
            EventFragment()
        )
    }
    private fun updateEvent() {
        viewModel.updateEvent(
            this.navigationArgs.eventId,
            this.binding.etEventTitle.text.toString(),
            this.binding.etEventCategory.text.toString(),
            this.binding.calendarView.date.toString()
        )
    }
}