package com.emissa.apps.events.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.emissa.apps.events.EventsApplication
import com.emissa.apps.events.EventsViewModel
import com.emissa.apps.events.EventsViewModelFactory
import com.emissa.apps.events.databinding.FragmentCreateEventBinding
import com.emissa.apps.events.model.Event
import java.text.SimpleDateFormat
import java.util.*


class CreateEventFragment : Fragment() {
    private val navigationArgs: EventDetailsFragmentArgs by navArgs()
    lateinit var event: Event
    lateinit var selectedDate: String

    private val viewModel: EventsViewModel by activityViewModels {
        EventsViewModelFactory(
            (requireActivity()?.application as EventsApplication).database.eventDao()
        )
    }
    private var mBinding: FragmentCreateEventBinding? = null
    private val binding get() = mBinding!!

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
        val calendarView = binding.calendarView


        // set events date based on user's selection or the actual date
        // need to work on  condition statement to handle this efficiently
        val date: Long = calendarView.date
        selectedDate = SimpleDateFormat("MM/dd/yyyy").format(date)
        Log.d("CreateEventFrag", "Selected date is $selectedDate")

        calendarView.setOnDateChangeListener { _, year, month, day ->
            Log.d("CreateEventFrag", "User selecting a date")
            val currentMonth: Int = month + 1
            selectedDate = "$currentMonth/$day/$year"
            Log.d("CreateEventFrag", "Selected date is $selectedDate")
        }

        val id = navigationArgs.eventId
        if (id > 0) {
            viewModel.retrieveEvent(id).observe(this.viewLifecycleOwner) { selectedEvent ->
                event = selectedEvent
                bindEvent(event)
            }
        } else {
            binding.btnAddEvent.setOnClickListener {
                addNewEvent()
            }
        }
    }

    private fun bindEvent(event: Event) {
        binding.apply {
            etEventTitle.setText(event.title)
            etEventCategory.setText(event.category)
        }
    }

    private fun addNewEvent() {
        viewModel.addNewEvent(
            binding.etEventTitle.text.toString(),
            binding.etEventCategory.text.toString(),
            selectedDate
        )
        val action = CreateEventFragmentDirections.actionCreateEventsToEventsFragment()
        this.findNavController().navigate(action)
    }
    private fun updateEvent() {
        viewModel.updateEvent(
            this.navigationArgs.eventId,
            this.binding.etEventTitle.text.toString(),
            this.binding.etEventCategory.text.toString(),
            this.binding.calendarView.date.toString()
        )
        val action = CreateEventFragmentDirections.actionCreateEventsToEventsFragment()
        this.findNavController().navigate(action)
    }
}