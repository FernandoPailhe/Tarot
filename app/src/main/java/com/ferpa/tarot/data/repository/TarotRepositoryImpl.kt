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
        val LAST_GAME_DATE = longPreferencesKey("last_game_date")
    }

    override fun getShuffledDeck(): List<Card> {
        return source.deck.cards.shuffled()
    }

    override suspend fun setLastGameDate() {
        context.dataStore.edit {
            it[LAST_GAME_DATE] = Calendar.getInstance().timeInMillis
        }
    }

    override suspend fun getLastGameDate(): Long? {
        return context.dataStore.data.first()[LAST_GAME_DATE]
    }

}