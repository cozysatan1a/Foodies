package com.cozysatan1a.foodies.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cozysatan1a.foodies.databinding.LayoutRecipeContainerBinding
import com.cozysatan1a.foodies.models.FoodRecipe
import com.cozysatan1a.foodies.models.Result
import com.cozysatan1a.foodies.ui.DetailsActivity
import com.cozysatan1a.foodies.util.RecipesDiffUtil

/**
 * Created by animsh on 2/27/2021.
 */
class RecipesAdapter(
    private var activity: Activity
) : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    private var recipes = emptyList<Result>()

    class RecipeViewHolder(private val binding: LayoutRecipeContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecipeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutRecipeContainerBinding.inflate(layoutInflater, parent, false)
                return RecipeViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeViewHolder {
        return RecipeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
        holder.itemView.setOnClickListener {
            val intent = Intent(activity, DetailsActivity::class.java)
            intent.putExtra("result", currentRecipe)
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setData(newData: FoodRecipe) {
        val recipesDiffUtil = RecipesDiffUtil(recipes, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }

}