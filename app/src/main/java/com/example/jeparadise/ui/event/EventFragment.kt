package com.example.jeparadise.ui.event

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeparadise.R
import com.example.jeparadise.databinding.FragmentEventBinding
import com.example.jeparadise.ui.place.PlaceAdapter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class EventFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    private var binding: FragmentEventBinding? = null
    private val eventSnapshotList = mutableListOf<DocumentSnapshot>()
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        val event = view.findViewById<RecyclerView>(R.id.event)

        // RecyclerView
        eventAdapter = EventAdapter(eventSnapshotList)
        event.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        event.adapter = eventAdapter

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        db.collection("event")
            .orderBy("name")
            .get().addOnSuccessListener { result ->
                eventSnapshotList.clear()
                eventSnapshotList.addAll(result.documents)
                eventAdapter.notifyDataSetChanged()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}