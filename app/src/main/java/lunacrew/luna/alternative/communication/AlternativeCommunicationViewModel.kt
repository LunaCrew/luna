package lunacrew.luna.alternative.communication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lunacrew.luna.database.AppDatabase
import lunacrew.luna.database.entities.AltCommunicationEntity
import javax.inject.Inject

@HiltViewModel
class AlternativeCommunicationViewModel @Inject constructor(
    private val db: AppDatabase
): ViewModel() {
    private val _boxTexts = MutableStateFlow(emptyList<AltCommunicationEntity>())

    val boxTextState: StateFlow<List<AltCommunicationEntity>> = _boxTexts.asStateFlow()

    init {
        getFavoriteBooks()
    }

    fun getFavoriteBooks() {
        viewModelScope.launch {
            db.altCommunicationDao().getAllTexts().flowOn(Dispatchers.IO).collect { texts: List<AltCommunicationEntity> ->
                _boxTexts.update { texts }
            }
        }
    }

    fun deleteText(entity: AltCommunicationEntity){
        viewModelScope.launch {
            db.altCommunicationDao().deleteText(entity)
        }
    }

    fun updateText(entity: AltCommunicationEntity){
        viewModelScope.launch {
            db.altCommunicationDao().updateText(entity)
        }
    }

    fun insertText(entity: AltCommunicationEntity){
        viewModelScope.launch {
            db.altCommunicationDao().insertText(entity)
        }
    }


}
