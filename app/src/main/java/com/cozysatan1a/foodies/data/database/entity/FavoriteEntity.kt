package com.cozysatan1a.foodies.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cozysatan1a.foodies.models.Result
import com.cozysatan1a.foodies.util.Constants.Companion.FAVORITE_RECIPE_TABLE

/**
 * Created by animsh on 4/30/2021.
 */
@Entity(tableName = FAVORITE_RECIPE_TABLE)
class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)