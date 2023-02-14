package com.ferpa.tarot.data.repository

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.ferpa.tarot.data.Source
import com.ferpa.tarot.data.repository.TarotRepositoryImpl.Companion.PREFERENCES_NAME
import com.ferpa.tarot.domain.model.Card
import com.ferpa.tarot.domain.repository.TarotRepository
import kotlinx.coroutines.flow.first
import java.util.*

val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

class TarotRepositoryImpl(private val source: Source, private val context: Context): TarotRepository {

    companion object {
        const val PREFERENCES_NAME = "user"
        val FIRST_GAME_DATE = longPreferencesKey("first_game_date")
    }

    override fun getShuffledDeck(): List<Card> {
        return source.deck.cards.shuffled()
    }

    override suspend fun setFirstTarotReadingDate(): Boolean {
        return if (context.dataStore.data.first()[FIRST_GAME_DATE] != null ) {
            false
        } else {
            context.dataStore.edit {
                it[FIRST_GAME_DATE] = Calendar.getInstance().timeInMillis
            }
            true
        }
    }

    override suspend fun getLastGameDate(): Long? {
        return context.dataStore.data.first()[FIRST_GAME_DATE]
    }

}