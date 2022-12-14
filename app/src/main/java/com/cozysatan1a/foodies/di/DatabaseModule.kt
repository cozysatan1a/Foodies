package com.cozysatan1a.foodies.di

import android.content.Context
import androidx.room.Room
import com.cozysatan1a.foodies.data.database.RecipesDatabase
import com.cozysatan1a.foodies.util.Constants.Companion.RECIPE_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by animsh on 3/9/2021.
 */
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(
        context,
        RecipesDatabase::class.java,
        RECIPE_DATABASE
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: RecipesDatabase) = database.recipeDao()
}