package com.example.jeparadise.ui.culinary

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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class CulinaryFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    private var binding: FragmentPlaceBinding? = null
    private val culinarySnapshotList = mutableListOf<DocumentSnapshot>()
    private lateinit var culinaryAdapter: CulinaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_culinary, container, false)
        val culinary = view.findViewById<RecyclerView>(R.id.culinary)

        // RecyclerView
        culinaryAdapter = CulinaryAdapter(culinarySnapshotList)
        culinary.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        culinary.adapter = culinaryAdapter

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        db.collection("culinary")
            .orderBy("name")
            .get().addOnSuccessListener { result ->
                culinarySnapshotList.clear()
                culinarySnapshotList.addAll(result.documents)
                culinaryAdapter.notifyDataSetChanged()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}