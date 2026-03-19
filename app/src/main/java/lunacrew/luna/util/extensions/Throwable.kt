package lunacrew.luna.util.extensions

import android.util.Log
import io.sentry.Sentry

fun Throwable.log(tag: String) {
    Log.e(tag, this.stackTraceToString())
    Sentry.captureException(this)
}

fun Throwable.log(tag: String, msg: String) {
    Log.e(tag, msg, this)
    Sentry.captureException(this)
}
