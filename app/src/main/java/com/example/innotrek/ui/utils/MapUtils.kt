package com.example.innotrek.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.getStoreNameFromResource(resId: Int): String {
    return this.resources.getResourceEntryName(resId)
        .removePrefix("Point_")
        .replace("_", " ")
}

 fun openMapsUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

// Movemos esta función a MapUtils.kt o la hacemos pública
fun openMapsUrlPublic(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}