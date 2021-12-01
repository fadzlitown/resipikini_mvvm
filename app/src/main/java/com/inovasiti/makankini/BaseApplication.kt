package com.inovasiti.makankini

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Must annotate the Application class.
@HiltAndroidApp
class BaseApplication : Application() {
}