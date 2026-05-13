package hu.magyarvandor.app.data

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Képfeltöltés Firebase Storage-ba.
 * A feltöltés után a letöltési URL-t adja vissza.
 */
@Singleton
class FirebaseImageUploader @Inject constructor(
    private val storage: FirebaseStorage
) {

    suspend fun uploadImage(uriString: String): String {
        val uri = Uri.parse(uriString)

        val fileName = "place_images/${System.currentTimeMillis()}_${uri.lastPathSegment ?: "image"}"
        val ref = storage.reference.child(fileName)

        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }
}