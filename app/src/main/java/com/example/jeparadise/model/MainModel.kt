package com.example.jeparadise.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class MainModel (
    var distance : Double? = 0.0,
    var location : String? = null,
    var name : String? = null,
    var url : String? = null,
    var description : String? = null,
    var category : String? = null,
    var geo: @RawValue GeoPoint? = null
) : Parcelable {

    fun getLatLng(): LatLng? {
        return geo?.let { LatLng(it.latitude, it.longitude) }
    }
    override fun describeContents(): Int {
        return 0
    }

}