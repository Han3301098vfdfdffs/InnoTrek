package com.example.innotrek.data

import com.example.innotrek.R
import com.example.innotrek.model.Stores
import com.example.innotrek.model.Points


class DataMaps(){
    fun LoadStores():List<Stores>{
        return listOf<Stores>(
            Stores(R.string.UNIT_Centro),
            Stores(R.string.UNIT_Copilco),
            Stores(R.string.UNIT_Zacatenco),
            Stores(R.string.UNIT_Guadalajara),
            Stores(R.string.Kinetronica),
            Stores(R.string.SandoRobotics),
        )
    }
    fun LoadPoints():List<Points>{
        return listOf<Points>(
            Points(R.string.Point_UNIT_Centro),
            Points(R.string.Point_UNIT_Copilco),
            Points(R.string.Point_UNIT_Zacatenco),
            Points(R.string.Point_UNIT_Guadalajara),
            Points(R.string.Point_Kinetronica),
            Points(R.string.Point_SandoRobotics),
        )
    }
}
