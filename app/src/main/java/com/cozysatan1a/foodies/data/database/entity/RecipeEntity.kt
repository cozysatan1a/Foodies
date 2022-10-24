package com.cozysatan1a.foodies.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cozysatan1a.foodies.models.FoodRecipe
import com.cozysatan1a.foodies.util.Constants.Companion.RECIPE_TABLE

/**
 * Created by animsh on 3/6/2021.
 */
@Entity(tableName = RECIPE_TABLE)
class RecipeEntity(
    val foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}