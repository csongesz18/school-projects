package hu.magyarvandor.app.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import hu.magyarvandor.app.ui.util.categoryIconRes // 🔥 EZ A LÉNYEG

// Ez a függvény visszaad egy Google Maps marker ikont kategória alapján
fun getMarkerIcon(context: Context, category: String): BitmapDescriptor {

    val resId = categoryIconRes(category)

    val bitmap = BitmapFactory.decodeResource(context.resources, resId)

    val sizePx = dpToPx(context, 48f)

    val scaled: Bitmap = Bitmap.createScaledBitmap(bitmap, sizePx, sizePx, true)

    return BitmapDescriptorFactory.fromBitmap(scaled)
}

// Segédfüggvény: dp → pixel
private fun dpToPx(context: Context, dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}