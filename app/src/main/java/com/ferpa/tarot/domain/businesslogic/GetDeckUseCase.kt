package com.ferpa.tarot.domain.businesslogic

import com.ferpa.tarot.domain.model.Card
import com.ferpa.tarot.domain.repository.TarotRepository
import javax.inject.Inject

class GetDeckUseCase @Inject constructor(
    private val repository: TarotRepository
) {

    operator fun invoke(): List<Card> {
        return repository.getShuffledDeck()
    }

}