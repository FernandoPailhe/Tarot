package com.ferpa.tarot.domain.repository

import com.ferpa.tarot.domain.model.Card

interface TarotRepository {

    fun getShuffledDeck(): List<Card>

    suspend fun setLastGameDate()

    suspend fun getLastGameDate(): Long?

}