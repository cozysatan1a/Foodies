package com.cozysatan1a.foodies.viewmodels

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.cozysatan1a.foodies.data.DataStoreRepository
import com.cozysatan1a.foodies.util.Constants.Companion.API_KEY
import com.cozysatan1a.foodies.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.cozysatan1a.foodies.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.cozysatan1a.foodies.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.cozysatan1a.foodies.util.Constants.Companion.QUERY_ADD_NUTRITION
import com.cozysatan1a.foodies.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.cozysatan1a.foodies.util.Constants.Companion.QUERY_API_KEY
import com.cozysatan1a.foodies.util.Constants.Companion.QUERY_DIET
import com.cozysatan1a.foodies.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.cozysatan1a.foodies.util.Constants.Companion.QUERY_INSTRUCTION_REQUIRED
import com.cozysatan1a.foodies.util.Constants.Companion.QUERY_NUMBER
import com.cozysatan1a.foodies.util.Constants.Companion.QUERY_SEARCH
import com.cozysatan1a.foodies.util.Constants.Companion.QUERY_SORT
import com.cozysatan1a.foodies.util.Constants.Companion.QUERY_SORT_DIRECTION
import com.cozysatan1a.foodies.util.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    var networkStatus = false
    var backOnline = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    private val mutableSelectedItem = MutableLiveData<Boolean>()
    val backFrom: LiveData<Boolean> get() = mutableSelectedItem

    fun setBackFrom(item: Boolean) {
        mutableSelectedItem.value = item
    }

    private val mutableOnlineItem = MutableLiveData<Boolean>()
    val onlineStatus: LiveData<Boolean> get() = mutableOnlineItem

    fun setOnlineStatus(item: Boolean) {
        mutableOnlineItem.value = item
    }

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }

    private fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }

    fun applyQueries(context: Context, recipeNumber: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMealAndDietType.collect { values ->
                mealType = values.selectedMealType
                dietType = values.selectedDietType
            }
        }

        val sharedPreference =
            context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        mealType = sharedPreference.getString("meal", DEFAULT_MEAL_TYPE).toString()
        dietType = sharedPreference.getString("diet", DEFAULT_DIET_TYPE).toString()
        queries[QUERY_NUMBER] = recipeNumber
        queries[QUERY_API_KEY] = API_KEY
        if (recipeNumber != "5") {
            queries[QUERY_TYPE] = mealType
            queries[QUERY_DIET] = dietType
        }
        queries[QUERY_INSTRUCTION_REQUIRED] = "true"
        queries[QUERY_SORT] = "popularity"
        queries[QUERY_SORT_DIRECTION] = "asc"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        queries[QUERY_ADD_NUTRITION] = "true"

        return queries
    }

    fun applySearch(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_INSTRUCTION_REQUIRED] = "true"
        queries[QUERY_SORT] = "popularity"
        queries[QUERY_SORT_DIRECTION] = "asc"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        queries[QUERY_ADD_NUTRITION] = "true"

        return queries
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection!!", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "We're back online.", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }

}