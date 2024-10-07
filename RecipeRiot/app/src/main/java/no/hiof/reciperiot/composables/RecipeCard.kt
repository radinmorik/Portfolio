package no.hiof.reciperiot.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import no.hiof.reciperiot.R
import no.hiof.reciperiot.ViewModels.RecipeViewModel
import no.hiof.reciperiot.model.Recipe

@Composable
fun RecipeCard(
    recipe: Recipe,
    onRecipeClick: (String) -> Unit,
    onFavouriteToggle: (Recipe) -> Unit,
    updateRecipeFavouriteStatus: (Recipe, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    recipeViewModel: RecipeViewModel = viewModel()
) {
    var favourite by rememberSaveable { mutableStateOf(recipe.favourite) }
    recipeViewModel.getNutrition(recipe = recipe)

    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onRecipeClick(recipe.id) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.width(200.dp)){
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(recipe.cookingTime)
                Text("Calories: ${recipeViewModel.calories}")
            }
            Column(modifier = Modifier.width(with(LocalDensity.current) { 256.toDp() }),
                horizontalAlignment = Alignment.End){
                AsyncImage(
                    model = recipe.imageURL,
                    contentDescription = "Image of the recipe",
                    error = painterResource(id = R.drawable.standardfood))
                IconToggleButton(
                    checked = favourite,
                    onCheckedChange = {
                        favourite = !favourite
                        onFavouriteToggle(recipe.copy(favourite = favourite))

                        updateRecipeFavouriteStatus(recipe, favourite)

                        Log.d("RecipeCard", "Favourite toggled for recipe: ${recipe.title}, favourite: $favourite")
                    }) {
                    Icon(
                        imageVector = if (favourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = modifier.graphicsLayer {
                            scaleX = 1.3f
                            scaleY = 1.3f
                        },
                    )
                }
            }
        }
    }
}