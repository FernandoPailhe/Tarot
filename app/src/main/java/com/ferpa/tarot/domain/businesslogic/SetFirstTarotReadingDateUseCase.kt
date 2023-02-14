package com.ferpa.tarot.domain.businesslogic

import com.ferpa.tarot.domain.repository.TarotRepository
import javax.inject.Inject

class SetFirstTarotReadingDateUseCase @Inject constructor(
    private val repository: TarotRepository
) {

    suspend operator fun invoke(): Boolean {
        return repository.setFirstTarotReadingDate()
    }

}