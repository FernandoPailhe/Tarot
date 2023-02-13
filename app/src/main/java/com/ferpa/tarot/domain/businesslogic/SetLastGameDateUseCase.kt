package com.ferpa.tarot.domain.businesslogic

import com.ferpa.tarot.domain.repository.TarotRepository
import javax.inject.Inject

class SetLastGameDateUseCase @Inject constructor(
    private val repository: TarotRepository
) {

    suspend operator fun invoke() {
        repository.setLastGameDate()
    }

}