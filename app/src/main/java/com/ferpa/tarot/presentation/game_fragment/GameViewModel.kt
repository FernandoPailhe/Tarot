package com.ferpa.tarot.presentation.game_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferpa.tarot.domain.businesslogic.GetDeckUseCase
import com.ferpa.tarot.domain.businesslogic.SetLastTarotReadingDateUseCase
import com.ferpa.tarot.domain.model.Card
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val getDeckUseCase: GetDeckUseCase,
    private val setLastTarotReadingDateUseCase: SetLastTarotReadingDateUseCase
) : ViewModel() {

    private val _deck = MutableLiveData<List<Card>>(getDeckUseCase())
    val deck: LiveData<List<Card>> get() = _deck

    private val _selectedCards = MutableLiveData<List<Card>>(mutableListOf())
    val selectedCards : LiveData<List<Card>> get() = _selectedCards

    private val _alreadyBeenSenn = MutableLiveData(false)
    val alreadyBeenSenn : LiveData<Boolean> get() = _alreadyBeenSenn

    /**
     * Adds the selected Card object to the _selectedCards live data instance and marks it as
     * selected in the _deck live data instance.
     * @param cardIndex: Int
     */
    fun selectCard(cardIndex: Int) {
        val mutableList = _selectedCards.value?.toMutableList() ?: return
        val card = deck.value?.get(cardIndex) ?: return
        mutableList.add(card)
        _selectedCards.value = mutableList.toList()
        _deck.value = deck.value?.map {
            if (it == card) {
                it.copy(isSelected = true)
            } else {
                it
            }
        }
    }

    /**
     * Resets the _selectedCards and _deck live data instances to their initial state and sets the
     * _alreadyBeenSeen live data instance to false.
     */
    fun resetGame(){
        _selectedCards.value = mutableListOf()
        _deck.value = getDeckUseCase()
        _alreadyBeenSenn.value = false
    }

    /**
     * Sets the _alreadyBeenSeen live data instance to true and launches the setLastGameDateUseCase()
     * method within the viewModelScope.
     */
    fun setAlreadyBeenSeen() {
        _alreadyBeenSenn.value = true
        viewModelScope.launch {
            setLastTarotReadingDateUseCase()
        }
    }

}