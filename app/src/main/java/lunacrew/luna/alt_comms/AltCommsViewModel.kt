package lunacrew.luna.alt_comms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import lunacrew.luna.database.AppDatabase
import lunacrew.luna.database.entities.AltCommsEntity
import javax.inject.Inject

@HiltViewModel
class AltCommsViewModel @Inject constructor(
    private val db: AppDatabase
) : ViewModel() {
    private val _cardsList = MutableStateFlow<MutableList<AltCommsEntity>>(mutableListOf())
    val cardsList: StateFlow<MutableList<AltCommsEntity>> = _cardsList.asStateFlow()

    init {
        getCards()
    }

    fun getCards() {
        viewModelScope.launch {
            db.altCommunicationDao().getAllCards().flowOn(Dispatchers.IO)
                .collect { cards: List<AltCommsEntity> ->
                    _cardsList.value = cards.toMutableList()
                }
        }
    }

    fun deleteCards(cardIds: List<Int?>) {
        viewModelScope.launch {
            db.altCommunicationDao().deleteCards(cardIds)
        }
    }

    fun deleteCards(cardId: Int?) {
        viewModelScope.launch {
            db.altCommunicationDao().deleteCards(listOf(cardId))
        }
    }

    fun insertCard(entity: AltCommsEntity) {
        viewModelScope.launch {
            db.altCommunicationDao().insertCard(entity)
        }
    }

    fun reorderCards(entities: List<AltCommsEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            entities.forEachIndexed { id, entity ->
                db.altCommunicationDao().updateCard(entity.copy(id = id))
            }
        }
    }
}
