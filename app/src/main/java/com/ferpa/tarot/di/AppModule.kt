package com.ferpa.tarot.di

import android.content.Context
import com.ferpa.tarot.data.Source
import com.ferpa.tarot.data.repository.TarotRepositoryImpl
import com.ferpa.tarot.domain.repository.TarotRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTarotRepository(@ApplicationContext context: Context): TarotRepository = TarotRepositoryImpl(Source, context)

}