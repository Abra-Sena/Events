package com.emissa.apps.events.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.emissa.apps.events.EventsApplication
import com.emissa.apps.events.EventsViewModel
import com.emissa.apps.events.EventsViewModelFactory
import com.emissa.apps.events.R
import com.emissa.apps.events.databinding.FragmentCreateEventBinding
import com.emissa.apps.events.databinding.FragmentEventDetailsBinding
import com.emissa.apps.events.model.Event
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

class EventDetailsFragment : Fragment() {
//    private val binding by lazy { FragmentEventDetailsBinding.inflate(layoutInflater) }
    private val navigationArgs: EventDetailsFragmentArgs by navArgs()
    lateinit var event: Event

    private val viewModel: EventsViewModel by activityViewModels {
        EventsViewModelFactory(
            (requireActivity()?.application as EventsApplication).database.eventDao()
        )
    }

    private var mBinding: FragmentEventDetailsBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEventDetailsBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.eventId
        viewModel.retrieveEvent(id).observe(this.viewLifecycleOwner) {selectedEvent ->
            event = selectedEvent
            bindEvent(event)
        }
    }

    private fun bindEvent(event: Event) {
        binding.apply {
            eventTitle.setText(event.title, TextView.BufferType.EDITABLE)
            eventCategory.setText(event.category, TextView.BufferType.EDITABLE)
            tvDueDate.text = getDateDifference(event)
            calendarView.date = setDateCalendarView(event)
        }
    }

    private fun setDateCalendarView(event: Event): Long {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
        val date = dateFormat.parse(event.date)
        return date.time
    }

    private fun getDateDifference(event: Event): String {
        val today = Calendar.getInstance()

        val currDay = today.get(Calendar.DAY_OF_MONTH)
        val currMonth = today.get(Calendar.MONTH) + 1
        val currYear = today.get(Calendar.YEAR)
        val dueDate = event.date.split('/')

        val dueDateDay: Int? = dueDate[1].toInt()
        val dueDateMonth: Int? = dueDate[0].toInt()
        val dueDateYear: Int? = dueDate[2].toInt()

        val dueToday = (currDay == dueDateDay) && (currMonth == dueDateMonth) && (currYear == dueDateYear)
        val pastDueDay = (currYear == dueDateYear) && (currMonth == dueDateMonth) && (currDay > dueDateDay!!)
        val pastDueMonth = (currYear == dueDateYear) && (currMonth > dueDateMonth!!)

        val dayDiff = dueDateDay!! - currDay
        val monthDiff = dueDateMonth!! - currMonth
        val yearDiff = dueDateYear!! - currYear

        return if (dueToday) "Due date is today!"
        else if (pastDueDay) "Event was due ${dayDiff.absoluteValue} day(s) ago!"
        else if (pastDueMonth) {
            if (dayDiff == 0) "Event was due ${monthDiff.absoluteValue} month(s) ago!"
            else "Event was due ${monthDiff.absoluteValue} month(s) " +
                    "and ${dayDiff.absoluteValue} day(s) ago!"
        }
        else if (yearDiff == 0 && monthDiff == 0) "Event due in ${dayDiff.absoluteValue} day(s)"
        else if (yearDiff == 0) "Event due in ${monthDiff.absoluteValue} month(s), " +
                "${dayDiff.absoluteValue} day(s)"
        else "Event due in ${yearDiff.absoluteValue} year(s), ${monthDiff.absoluteValue} " +
                "month(s), and ${dayDiff.absoluteValue} day(s)"
    }
}