package lunacrew.luna.util.accessibility

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import lunacrew.luna.util.extensions.getActivity
import lunacrew.luna.util.extensions.normalizeToFloat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TtsViewModel @Inject constructor() : ViewModel() {
    private val _tts = MutableStateFlow<TextToSpeech?>(null)
    val tts: StateFlow<TextToSpeech?> = _tts.asStateFlow()

    private val _isSpeaking = MutableStateFlow<Boolean>(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val totalLength = MutableLiveData<Int>(0)

    private val _progress = MutableStateFlow<Float>(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _clickedId = MutableStateFlow<Int?>(null)
    val clickedId: StateFlow<Int?> = _clickedId.asStateFlow()

    fun instance(context: Context): TextToSpeech {
        var instance: TextToSpeech? = null
        instance = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                instance?.language = Locale.getDefault()
            }
        }
        this._tts.value = instance
        return instance
    }

    fun talk(context: Context, text: String) {
        setupProgressListener(text)
        val utteranceId = context.getActivity().hashCode().toString()
        this._tts.value?.speak(text, TextToSpeech.QUEUE_ADD, null, utteranceId)
    }

    fun setClickedId(id: Int?) {
        _clickedId.value = id
    }

    private fun setupProgressListener(text: String) {
        totalLength.postValue(text.length)

        this._tts.value?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                Log.i("TTS", "onStart: $utteranceId")
                _isSpeaking.value = false
            }

            override fun onStop(utteranceId: String?, interrupted: Boolean) {
                super.onStop(utteranceId, interrupted)
                Log.i("TTS", "onStop: $utteranceId")
                _isSpeaking.value = false
            }

            override fun onDone(utteranceId: String?) {
                Log.i("TTS", "onDone: $utteranceId")
                _progress.value = (text.length.toFloat())
                _isSpeaking.value = false
            }


            @Deprecated("Deprecated in Java")
            override fun onError(p0: String?) {
                Log.e("TTS", "onError: $p0")
                _isSpeaking.value = false
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                Log.e("TTS", "onError: $errorCode :: id: $utteranceId")
                _isSpeaking.value = false
            }

            override fun onRangeStart(utteranceId: String?, start: Int, end: Int, frame: Int) {
                Log.i(
                    "TTS",
                    "onRangeStart :: utteranceId= $utteranceId :: start= $start :: frame= $frame :: end= $end"
                )
                _progress.value = (start.normalizeToFloat(totalLength.value ?: 0))
            }
        })
    }
}
