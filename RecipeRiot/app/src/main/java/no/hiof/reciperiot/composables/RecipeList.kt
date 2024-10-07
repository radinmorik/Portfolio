package no.hiof.reciperiot.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import no.hiof.reciperiot.Screen
import no.hiof.reciperiot.model.Recipe

@Composable
fun RecipeList(
    recipes: List<Recipe>,
    navController: NavController,
    onFavouriteToggle: (Recipe) -> Unit,
    updateRecipeFavouriteStatus: (Recipe, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(userScrollEnabled = true, modifier = modifier) {
        items(recipes) { recipe ->
            RecipeCard(
                recipe,
                onRecipeClick = { selectedRecipe ->
                    navController.navigate("${Screen.RecipePage.route}/${selectedRecipe}")
                },
                onFavouriteToggle = onFavouriteToggle,
                updateRecipeFavouriteStatus = updateRecipeFavouriteStatus
            )
        }
    }
}