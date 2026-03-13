package lunacrew.luna.util.accessibility

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

fun textToSpeech(context: Context): TextToSpeech  {
    var ttsInstance: TextToSpeech? = null
    ttsInstance = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            ttsInstance?.language = Locale.getDefault()
        }
    }
    return ttsInstance
}
