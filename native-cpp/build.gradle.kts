plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "org.dany"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

tasks.register("runCpp") {
    dependsOn("externalNativeBuildDebug")
    doLast {
        val executable = file("build/intermediates/cmake/debug/obj/arm64-v8a/cpp")
        exec {
            setExecutable("adb")
            args("push", executable.absolutePath, "/data/local/tmp")
        }
        exec {
            setExecutable("adb")
            args("shell", "/data/local/tmp/cpp")
        }
    }
}