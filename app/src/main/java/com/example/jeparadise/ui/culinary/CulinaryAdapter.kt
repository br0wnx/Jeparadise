package com.example.jeparadise.ui.culinary

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

class CulinaryAdapter (private val culinarySnapshotList: List<DocumentSnapshot>) : RecyclerView.Adapter<CulinaryAdapter.ViewHolder>() {

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val culinaryImage = view.findViewById<ImageView>(R.id.culinary_img)
        val culinaryName = view.findViewById<TextView>(R.id.culinary_name)
        val culinaryCategory = view.findViewById<TextView>(R.id.culinary_category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.list_culinary, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val document: DocumentSnapshot = culinarySnapshotList[position]
        val data = document.toObject(MainModel::class.java)
        if (data != null) {
            Picasso.get().load(data.url).into(holder.culinaryImage)
            holder.culinaryName.text = data.name
            holder.culinaryCategory.text = data.category

            holder.itemView.setOnClickListener {
                val intent = Intent(it.context, DetailActivity::class.java)
                intent.putExtra("name", data.name)
                intent.putExtra("url", data.url)
                intent.putExtra("location", data.location)
                intent.putExtra("category", data.category)
                intent.putExtra("description", data.description)
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return culinarySnapshotList.size
    }
}