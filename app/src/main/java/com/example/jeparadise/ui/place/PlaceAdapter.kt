package com.example.jeparadise.ui.place

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jeparadise.R
import com.example.jeparadise.model.MainModel
import com.example.jeparadise.ui.detail.DetailActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.squareup.picasso.Picasso

class PlaceAdapter (private val placeSnapshotList: List<DocumentSnapshot>) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val placeImage = view.findViewById<ImageView>(R.id.place_img)
        val placeName = view.findViewById<TextView>(R.id.place_name)
        val placeLocation = view.findViewById<TextView>(R.id.place_location)
        val placeDistance = view.findViewById<TextView>(R.id.place_distance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.list_place, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val document: DocumentSnapshot = placeSnapshotList[position]
        val data = document.toObject(MainModel::class.java)
        if (data != null) {
            Picasso.get().load(data.url).into(holder.placeImage)
            holder.placeName.text = data.name
            holder.placeLocation.text = data.location
            holder.placeDistance.text = data.distance.toString() + " km"

            holder.itemView.setOnClickListener {
                val intent = Intent(it.context, DetailActivity::class.java)
                intent.putExtra("name", data.name)
                intent.putExtra("url", data.url)
                intent.putExtra("location", data.location)
                intent.putExtra("distance", data.distance.toString() + " km")
                intent.putExtra("description", data.description)
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return placeSnapshotList.size
    }
}