package com.cozysatan1a.foodies.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cozysatan1a.foodies.adapters.FavRecipeAdapter
import com.cozysatan1a.foodies.data.database.entity.FavoriteEntity
import com.cozysatan1a.foodies.data.database.entity.RecipeEntity
import com.cozysatan1a.foodies.models.FoodRecipe
import com.cozysatan1a.foodies.util.NetworkResult
import com.makeramen.roundedimageview.RoundedImageView

/**
 * Created by animsh on 3/10/2021.
 */
class RecipeBindingAdapter {

    companion object {

        @BindingAdapter("android:readApiResponse", "android:readDatabase", requireAll = true)
        @JvmStatic
        fun setError(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipe?>?,
            database: List<RecipeEntity?>?
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            } else if (apiResponse is NetworkResult.Loading) {
                textView.visibility = View.INVISIBLE
            } else if (apiResponse is NetworkResult.Success) {
                textView.visibility = View.INVISIBLE
            }

        }

        @BindingAdapter("android:viewVisibility", "android:setData", requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favoritesEntity: List<FavoriteEntity>?,
            adapter: FavRecipeAdapter?
        ) {
            if (favoritesEntity.isNullOrEmpty()) {
                when (view) {
                    is RoundedImageView -> {
                        view.visibility = View.VISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.VISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.INVISIBLE
                    }
                }
            } else {
                when (view) {
                    is ImageView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        adapter?.setData(favoritesEntity)
                    }
                }
            }
        }
    }
}