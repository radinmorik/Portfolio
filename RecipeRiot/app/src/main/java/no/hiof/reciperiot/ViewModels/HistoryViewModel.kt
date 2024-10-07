package no.hiof.reciperiot.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import no.hiof.reciperiot.data.RecipeRepository
import no.hiof.reciperiot.model.Recipe

class HistoryViewModel: ViewModel() {
    val history by mutableStateOf(RecipeRepository().loadRecipes())
    var searchText by  mutableStateOf("")

    private val recipeRepository = RecipeRepository()

    fun updateRecipeFavouriteStatus(recipe: Recipe, fav: Boolean) {
        recipeRepository.updateRecipeFavouriteStatus(recipe, fav)
    }
}