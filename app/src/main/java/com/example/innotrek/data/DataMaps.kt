package com.example.innotrek.data

import android.content.Context
import com.example.innotrek.R
import com.example.innotrek.data.model.Point
import com.example.innotrek.data.model.Store
import com.google.android.gms.maps.model.LatLng

class DataMaps(private val context: Context) {
    fun loadStoresWithPoints(): List<Store> {
        val stores = listOf(
            Store(R.string.UNIT_Centro),
            Store(R.string.UNIT_Copilco),
            Store(R.string.UNIT_Zacatenco),
            Store(R.string.UNIT_Guadalajara),
            Store(R.string.Kinetronica),
            Store(R.string.SandoRobotics)
        )

        val points = listOf(
            Point(R.string.Point_UNIT_Centro),
            Point(R.string.Point_UNIT_Copilco),
            Point(R.string.Point_UNIT_Zacatenco),
            Point(R.string.Point_UNIT_Guadalajara),
            Point(R.string.Point_Kinetronica),
            Point(R.string.Point_SandoRobotics)
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