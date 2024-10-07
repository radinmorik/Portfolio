package no.hiof.reciperiot.ViewModels


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import no.hiof.reciperiot.data.IngredientsRepository

class IngredientsViewModel : ViewModel() {
    var newIngredient by mutableStateOf("")
    var ingredientsList by mutableStateOf(emptyList<Pair<String, MutableState<Boolean>>>())

    private val ingredientRepository = IngredientsRepository()

    fun deleteIngredient(ingredientName: String) {
        ingredientRepository.deleteIngredient(ingredientName)
        updateIngredientsList()
    }

    fun saveIngredientsToDb() {
        val ingredientsToSave =
            ingredientsList.map { (name, checkedState) ->
                name to checkedState.value
            }
        ingredientRepository.saveIngredientsToDb(ingredientsToSave)
    }

    fun updateIngredientsList() {
        ingredientRepository.fetchIngredients() { data ->
            if (data != null) {
                val firestoreIngredients =
                    data.entries.map { it.key to mutableStateOf(it.value as Boolean) }
                ingredientsList = firestoreIngredients
            } else {
                println("No data or error")
            }
        }
    }
}