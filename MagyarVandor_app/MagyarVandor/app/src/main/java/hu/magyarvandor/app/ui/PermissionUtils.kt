package hu.magyarvandor.app.ui

import android.content.Context
import android.content.Intent
import android.net.Uri

// URI olvasási jog megőrzése újraindítás után (nem minden URI támogatja)
fun persistReadPermissionIfPossible(context: Context, uri: Uri) {
    try {
        val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        context.contentResolver.takePersistableUriPermission(uri, flags)
    } catch (_: SecurityException) {
        // nem minden URI persistable, ettől még működik
    } catch (_: Exception) {
    }
}