package com.ferpa.tarot.presentation.menu_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferpa.tarot.domain.businesslogic.GetLastTarotReadingDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getLastTarotReadingDateUseCase: GetLastTarotReadingDateUseCase
) : ViewModel() {

    private val _lastTarotReadingDate = MutableLiveData<Long?>(1)
    val lastTarotReadingDate: LiveData<Long?> get() = _lastTarotReadingDate

    init {
        viewModelScope.launch {
            _lastTarotReadingDate.value = getLastTarotReadingDateUseCase()
        }
    }

}