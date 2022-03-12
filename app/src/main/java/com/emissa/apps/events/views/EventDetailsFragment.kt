package com.emissa.apps.events.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.emissa.apps.events.EventsApplication
import com.emissa.apps.events.EventsViewModel
import com.emissa.apps.events.EventsViewModelFactory
import com.emissa.apps.events.R
import com.emissa.apps.events.databinding.FragmentCreateEventBinding
import com.emissa.apps.events.databinding.FragmentEventDetailsBinding
import com.emissa.apps.events.model.Event

class EventDetailsFragment : Fragment() {
//    private val binding by lazy { FragmentEventDetailsBinding.inflate(layoutInflater) }
    private val navigationArgs: EventDetailsFragmentArgs by navArgs()
    lateinit var event: Event

    private val viewModel: EventsViewModel by activityViewModels {
        EventsViewModelFactory(
            (activity?.application as EventsApplication).database.eventDao()
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
            tvEventTitle.text = event.title
            tvEventCategory.text = event.category
            tvEventDate.text = event.date
        }
    }
}