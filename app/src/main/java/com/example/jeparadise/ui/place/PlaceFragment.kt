package com.example.jeparadise.ui.place

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeparadise.R
import com.example.jeparadise.databinding.FragmentPlaceBinding
import com.example.jeparadise.ui.home.HomeAdapterTheMostVisited
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class PlaceFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    private var binding: FragmentPlaceBinding? = null
    private val placeSnapshotList = mutableListOf<DocumentSnapshot>()
    private lateinit var placeAdapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_place, container, false)
        val place = view.findViewById<RecyclerView>(R.id.place)

        // RecyclerView
        placeAdapter = PlaceAdapter(placeSnapshotList)
        place.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        place.adapter = placeAdapter

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        db.collection("place")
            .orderBy("name")
            .get().addOnSuccessListener { result ->
                placeSnapshotList.clear()
                placeSnapshotList.addAll(result.documents)
                placeAdapter.notifyDataSetChanged()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}