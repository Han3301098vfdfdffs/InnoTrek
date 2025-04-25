package com.example.innotrek.data

import android.content.Context
import com.example.innotrek.R
import com.example.innotrek.model.Points
import com.example.innotrek.model.Stores
import com.google.android.gms.maps.model.LatLng

class DataMaps(private val context: Context) {
    fun loadStoresWithPoints(): List<Stores> {
        val stores = listOf(
            Stores(R.string.UNIT_Centro),
            Stores(R.string.UNIT_Copilco),
            Stores(R.string.UNIT_Zacatenco),
            Stores(R.string.UNIT_Guadalajara),
            Stores(R.string.Kinetronica),
            Stores(R.string.SandoRobotics)
        )

        val points = listOf(
            Points(R.string.Point_UNIT_Centro),
            Points(R.string.Point_UNIT_Copilco),
            Points(R.string.Point_UNIT_Zacatenco),
            Points(R.string.Point_UNIT_Guadalajara),
            Points(R.string.Point_Kinetronica),
            Points(R.string.Point_SandoRobotics)
        )

        // Asumimos que el orden de stores y points coincide
        return stores.mapIndexed { index, store ->
            val pointString = context.getString(points[index].coordinatesResId)
            val (lat, lng) = pointString.split(",").map { it.trim().toDouble() }
            store.copy(point = LatLng(lat, lng))
        }
    }
}