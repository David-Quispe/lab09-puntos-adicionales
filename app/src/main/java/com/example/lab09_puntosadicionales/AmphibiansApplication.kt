package com.example.lab09_puntosadicionales

import android.app.Application
import com.example.lab09_puntosadicionales.data.AmphibiansContainer

class AmphibiansApplication : Application() {
    val container: AmphibiansContainer by lazy { AmphibiansContainer() }
}
