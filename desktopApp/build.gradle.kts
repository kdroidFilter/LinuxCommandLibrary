import dev.nucleusframework.desktop.application.dsl.CompressionLevel
import dev.nucleusframework.desktop.application.dsl.NativeImageOptimization
import dev.nucleusframework.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.nucleus)
}

group = "com.linuxcommandlibrary"
version = libs.versions.appVersion.get()

kotlin {
    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    sourceSets {
        getByName("desktopMain") {
            dependencies {
                implementation(project(":composeApp"))
                implementation(project(":common"))
                implementation(compose.desktop.currentOs)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.coroutines.core)

                // Nucleus: application entry point + Tao windowing backend + reactive dark mode
                implementation(libs.nucleus.application)
                implementation(libs.nucleus.decorated.window.tao)
                implementation(libs.nucleus.decorated.window.material3)
                implementation(libs.nucleus.darkmode.detector)
            }

            resources.srcDirs("../assets")
        }
    }
}

nucleus.application {
    mainClass = "com.linuxcommandlibrary.MainKt"

    graalvm {
        isEnabled.set(true)
        javaLanguageVersion.set(25)
        optimization = NativeImageOptimization.SIZE
    }

    nativeDistributions {
        targetFormats(TargetFormat.Dmg, TargetFormat.Nsis, TargetFormat.Deb, TargetFormat.Rpm, TargetFormat.AppImage, TargetFormat.Portable)
        packageName = "LinuxCommandLibrary"
        packageVersion = libs.versions.appVersion.get()
        cleanupNativeLibs = true
        compressionLevel = CompressionLevel.Ultra

        macOS {
            iconFile.set(project.file("icon.icns"))
        }
        windows {
            iconFile.set(project.file("icon.ico"))
        }
        linux {
            iconFile.set(project.file("icon.png"))
            modules("jdk.security.auth")
        }
    }
}
