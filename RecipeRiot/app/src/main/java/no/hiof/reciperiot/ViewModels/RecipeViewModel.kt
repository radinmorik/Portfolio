package no.hiof.reciperiot.ViewModels

import androidx.lifecycle.ViewModel
import no.hiof.reciperiot.data.RecipeRepository
import no.hiof.reciperiot.model.Recipe
import org.json.JSONArray
import org.json.JSONObject

class RecipeViewModel() : ViewModel() {
    private val recipeRepository = RecipeRepository()

    fun loadRecipe(recipeId: String): Recipe?{
        return recipeRepository.loadRecipes().firstOrNull { it.id == recipeId }
    }

    fun updateRecipeFavouriteStatus(recipe: Recipe, fav: Boolean) {
        recipeRepository.updateRecipeFavouriteStatus(recipe, fav)
    }

    var calories = "N/A"
    var protein = "N/A"
    var carbohydrates = "N/A"
    var fat = "N/A"
    fun getNutrition(recipe: Recipe){
        try{
            val nutrition = JSONObject(recipe.recipe_nutrition)
            calories = nutrition.getString("calories")
            protein = nutrition.getString("protein")
            carbohydrates = nutrition.getString("carbohydrates")
            fat = nutrition.getString("fat")
        }
        catch (e: Exception){
            print(e)
        }
    }

    var ingredients: JSONArray = JSONArray("[\"N/A\"]")
    fun getIngredients(recipe: Recipe){
        try{
            ingredients = JSONArray(recipe.recipe_ingredients)
        }
        catch (e: Exception){
            print(e)
        }
    }
}
