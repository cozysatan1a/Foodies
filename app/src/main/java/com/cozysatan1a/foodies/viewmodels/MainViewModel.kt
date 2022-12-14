package com.cozysatan1a.foodies.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.cozysatan1a.foodies.data.Repository
import com.cozysatan1a.foodies.data.database.entity.FavoriteEntity
import com.cozysatan1a.foodies.data.database.entity.RecipeEntity
import com.cozysatan1a.foodies.models.FoodRecipe
import com.cozysatan1a.foodies.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by animsh on 2/22/2021.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** Room */
    var readRecipe: LiveData<List<RecipeEntity>> = repository.local.readDatabase().asLiveData()
    var readFavRecipe: LiveData<List<FavoriteEntity>> =
        repository.local.readFavRecipe().asLiveData()

    private fun insertRecipe(recipeEntity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertRecipe(recipeEntity)
    }

    fun insertFavRecipe(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavRecipe(favoriteEntity)
        }

    fun deleteFavRecipe(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavRecipe(favoriteEntity)
        }

    fun deleteAllFavRecipe() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavRecipes()
        }

    /** RETROFIT */
    var foodRecipeResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var randomRecipeResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchRecipeResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()


    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(queries: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(queries)
    }

    fun getRandomRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRandomRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        foodRecipeResponse.value = NetworkResult.Loading()
        if (hasNetworkConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                foodRecipeResponse.value = handleResponse(response)

                val foodRecipe = foodRecipeResponse.value!!.data
                if (foodRecipe != null) {
                    offlineCacheRecipes(foodRecipe)
                }
            } catch (e: Exception) {
                foodRecipeResponse.value =
                    NetworkResult.Error(message = "Recipes not found!! " + e.message)
            }
        } else {
            foodRecipeResponse.value = NetworkResult.Error(message = "No Internet Connection!!")
        }
    }

    private suspend fun searchRecipesSafeCall(queries: Map<String, String>) {
        searchRecipeResponse.value = NetworkResult.Loading()
        if (hasNetworkConnection()) {
            try {
                val response = repository.remote.searchRecipes(queries)
                searchRecipeResponse.value = handleResponse(response)

            } catch (e: Exception) {
                searchRecipeResponse.value =
                    NetworkResult.Error(message = "Recipes not found!! " + e.message)
            }
        } else {
            searchRecipeResponse.value = NetworkResult.Error(message = "No Internet Connection!!")
        }
    }

    private suspend fun getRandomRecipesSafeCall(queries: Map<String, String>) {
        randomRecipeResponse.value = NetworkResult.Loading()
        if (hasNetworkConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                randomRecipeResponse.value = handleResponse(response)

                val foodRecipe = randomRecipeResponse.value!!.data
//                if (foodRecipe != null) {
//                    offlineCacheRecipes(foodRecipe)
//                }
            } catch (e: Exception) {
                randomRecipeResponse.value =
                    NetworkResult.Error(message = "Recipes not found!! " + e.message)
            }
        } else {
            randomRecipeResponse.value = NetworkResult.Error(message = "No Internet Connection!!")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipeEntity = RecipeEntity(foodRecipe)
        insertRecipe(recipeEntity)
    }

    private fun handleResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error(message = "Timeout!!")
            }

            response.code() == 402 -> {
                return NetworkResult.Error(message = "Quota Exceeded!!")
            }

            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error(message = "2 Recipe not found!!")
            }

            response.isSuccessful -> {
                val foodRecipe = response.body()
                return NetworkResult.Success(foodRecipe!!)
            }

            else -> return NetworkResult.Error(message = response.message())
        }
    }

    private fun hasNetworkConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}