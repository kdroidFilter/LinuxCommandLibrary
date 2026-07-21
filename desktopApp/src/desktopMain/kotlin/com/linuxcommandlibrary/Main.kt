package com.linuxcommandlibrary

import androidx.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState
import com.linuxcommandlibrary.app.App
import com.linuxcommandlibrary.app.di.commonModule
import com.linuxcommandlibrary.app.di.platformModule
import com.linuxcommandlibrary.app.ui.theme.LinuxTheme
import dev.nucleusframework.application.nucleusApplication
import dev.nucleusframework.darkmodedetector.isSystemInDarkMode
import dev.nucleusframework.window.material.MaterialDecoratedWindow
import dev.nucleusframework.window.material.MaterialTitleBar
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(commonModule, platformModule())
    }

    nucleusApplication() {
        val title = "Linux Command Library"
        val darkMode = isSystemInDarkMode()
        LinuxTheme(darkMode = darkMode) {
            MaterialDecoratedWindow(
                onCloseRequest = ::exitApplication,
                title = title,
                state = rememberWindowState(width = 900.dp, height = 700.dp),
            ) {
                MaterialTitleBar { Text(text = title, color = if (darkMode) Color.White else Color.Black ) }
                App(darkMode = darkMode)
            }
        }
    }
}
