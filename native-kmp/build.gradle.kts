plugins {
    alias(libs.plugins.kotlin.kmp)
}

kotlin {
    androidNativeArm64 {
        binaries {
            executable {
                binaryOptions["androidProgramType"] = "standalone"
                linkerOpts = mutableListOf("-z", "max-page-size=16384")
            }
        }
    }
}

tasks.register("runKmp") {
    dependsOn("linkDebugExecutableAndroidNativeArm64")
    doLast {
        val fileName = "native-kmp.kexe"
        val executable = file("build/bin/androidNativeArm64/debugExecutable/$fileName")
        exec {
            setExecutable("adb")
            args("push", executable.absolutePath, "/data/local/tmp")
        }
        // execute it. ulimit -c to enable core dumps
        val res = exec {
            isIgnoreExitValue = true
            setExecutable("adb")
            args("shell", "ulimit -c unlimited && cd /data/local/tmp && ./$fileName")
        }
        // pull dump on failure
        if (res.exitValue == 139) {
            val dump = file("build/dumps/androidNativeArm64/core")
            dump.parentFile!!.mkdirs()
            exec {
                setExecutable("adb")
                args("pull", "/data/local/tmp/core", dump.absolutePath)
            }
            println("Segfault! Analyze the core dump with the following command:")
            println("lldb --core ${dump.absolutePath} ${executable.absolutePath}")
        }
        // fail if failed
        res.rethrowFailure().assertNormalExitValue()
    }
}