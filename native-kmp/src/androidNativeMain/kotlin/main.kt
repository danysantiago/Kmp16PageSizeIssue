import platform.android.ANDROID_LOG_INFO
import platform.android.__android_log_write
import platform.posix.sleep

fun main() {
    println("KMP: Hello world! Sleeping for 5 seconds...")
    __android_log_write(ANDROID_LOG_INFO.toInt(),"KMP", "KN: Hello world! Sleeping for 5 seconds...");

    sleep(5u)

    println("KMP: Slept for 5 seconds! Exiting.")
    __android_log_write(ANDROID_LOG_INFO.toInt(),"KMP", "KN: Slept for 5 seconds! Exiting.");
}