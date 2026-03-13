package lunacrew.luna.alt_comms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lunacrew.luna.database.AppDatabase
import lunacrew.luna.database.entities.AltCommsEntity
import javax.inject.Inject

@HiltViewModel
class AltCommsViewModel @Inject constructor(
    private val db: AppDatabase
) : ViewModel() {
    private val _cardsList = MutableStateFlow(emptyList<AltCommsEntity>())
    val cardsList: StateFlow<List<AltCommsEntity>> = _cardsList

    init {
        getCards()
    }

    fun getCards() {
        viewModelScope.launch {
            db.altCommunicationDao().getAllCards().flowOn(Dispatchers.IO)
                .collect { texts: List<AltCommsEntity> ->
                    _cardsList.update { texts }
                }
        }
    }

    fun deleteCard(entity: AltCommsEntity) {
        viewModelScope.launch {
            db.altCommunicationDao().deleteCard(entity)
        }
    }

    fun updateCard(entity: AltCommsEntity) {
        viewModelScope.launch {
            db.altCommunicationDao().updateCard(entity)
        }
    }

    fun insertCard(entity: AltCommsEntity) {
        viewModelScope.launch {
            db.altCommunicationDao().insertCard(entity)
        }
    }

    fun reorderCards(entities: List<AltCommsEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            entities.forEachIndexed { position, entity ->
                db.altCommunicationDao().updateCard(entity.copy(position = position))
            }
        }
    }
}
