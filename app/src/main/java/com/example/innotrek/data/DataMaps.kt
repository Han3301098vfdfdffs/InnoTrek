package com.example.innotrek.data

import android.content.Context
import com.example.innotrek.R
import com.example.innotrek.data.model.Points
import com.example.innotrek.data.model.Stores
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

        return stores.mapIndexed { index, store ->
            val pointString = context.getString(points[index].coordinatesResId)
            val (lat, lng) = pointString.split(",").map { it.trim().toDouble() }

            val imageResId = when (store.nameResId) {
                R.string.Kinetronica -> R.drawable.logo_01_kinetronica
                R.string.SandoRobotics -> R.drawable.logo_02_sando
                else -> R.drawable.logo_00_unit
            }

            store.copy(
                point = LatLng(lat, lng),
                imageResId = imageResId
            )
        }
    }
}