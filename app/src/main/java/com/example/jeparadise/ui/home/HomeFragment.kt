package com.example.jeparadise.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeparadise.R
import com.example.jeparadise.databinding.FragmentHomeBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    private var binding: FragmentHomeBinding? = null
    private val theMostVisitedSnapshotList = mutableListOf<DocumentSnapshot>()
    private val whatsHappeningSnapshotList = mutableListOf<DocumentSnapshot>()
    private lateinit var homeAdapterTheMostVisited: HomeAdapterTheMostVisited
    private lateinit var homeAdapterWhatsHappening: HomeAdapterWhatsHappening

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val theMostVisited = view.findViewById<RecyclerView>(R.id.the_most_visited)
        val whatsHappening = view.findViewById<RecyclerView>(R.id.whats_happening)

        // RecyclerView
        homeAdapterTheMostVisited = HomeAdapterTheMostVisited(theMostVisitedSnapshotList)
        theMostVisited.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        theMostVisited.adapter = homeAdapterTheMostVisited

        homeAdapterWhatsHappening = HomeAdapterWhatsHappening(whatsHappeningSnapshotList)
        whatsHappening.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        whatsHappening.adapter = homeAdapterWhatsHappening

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        db.collection("theMostVisited")
            .orderBy("name")
            .get().addOnSuccessListener { result ->
                theMostVisitedSnapshotList.clear()
                theMostVisitedSnapshotList.addAll(result.documents)
                homeAdapterTheMostVisited.notifyDataSetChanged()
            }
        db.collection("whatsHappening")
            .get().addOnSuccessListener { result ->
                whatsHappeningSnapshotList.clear()
                whatsHappeningSnapshotList.addAll(result.documents)
                homeAdapterWhatsHappening.notifyDataSetChanged()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}