package lunacrew.luna.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import kotlinx.coroutines.Dispatchers
import lunacrew.luna.BuildConfig

val ktorClient = HttpClient(OkHttp) {
    engine {
        config {
            followRedirects(true)
        }
        dispatcher = Dispatchers.IO
    }

    install(Logging) {
        level = getLevel()
    }
}

private fun getLevel(): LogLevel {
    return if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.INFO
}
