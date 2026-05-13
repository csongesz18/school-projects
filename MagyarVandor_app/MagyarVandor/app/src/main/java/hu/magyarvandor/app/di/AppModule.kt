package hu.magyarvandor.app.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.magyarvandor.app.data.FirestorePlaceRepository
import hu.magyarvandor.app.data.PlaceRepository
import javax.inject.Singleton

/**
 * Hilt dependency injection modul.
 * Itt definiáljuk, hogy az alkalmazás milyen példányokat kapjon.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Firestore példány biztosítása.
     * Ez az online adatbázis, amit az alkalmazás használ.
     */
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Repository biztosítása (interface → implementáció kötés).
     * Az alkalmazás a Firestore alapú repository-t használja.
     */
    @Provides
    @Singleton
    fun providePlaceRepository(repo: FirestorePlaceRepository): PlaceRepository = repo

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()
}