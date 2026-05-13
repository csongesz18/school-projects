package hu.magyarvandor.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// Az alkalmazás belépési pontja.
// A @HiltAndroidApp szükséges a Dependency Injection (Hilt) működéséhez.
@HiltAndroidApp
class MagyarVandorApp : Application()