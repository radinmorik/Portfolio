package no.hiof.reciperiot.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import no.hiof.reciperiot.R
import no.hiof.reciperiot.Screen
import no.hiof.reciperiot.ViewModels.RecipeViewModel

@Composable
fun RecipePage1(navController: NavController, recipeId: String, recipeViewModel: RecipeViewModel = viewModel()) {
    val recipe = recipeViewModel.loadRecipe(recipeId)

    LazyColumn {
        if (recipe != null) {
            recipeViewModel.getNutrition(recipe = recipe)
            recipeViewModel.getIngredients(recipe = recipe)

            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = recipe.title,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .width(300.dp),
                            fontSize = 30.sp
                        )
                        var favourite by remember { mutableStateOf(recipe.favourite) }
                        IconToggleButton(
                            checked = favourite,
                            onCheckedChange = {
                                favourite = !favourite

                                recipeViewModel.updateRecipeFavouriteStatus(recipe, favourite)
                            },
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Icon(
                                imageVector = if (favourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier
                                    .graphicsLayer {
                                        scaleX = 1.3f
                                        scaleY = 1.3f
                                    },
                            )
                        }
                    }
                    AsyncImage(
                        model = recipe.imageURL,
                        contentDescription = "Image of the recipe",
                        modifier = Modifier.height(400.dp),
                        error = painterResource(id = R.drawable.standardfood)
                    )
                    Text(
                        text = stringResource(R.string.cooktime, recipe.cookingTime),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        fontSize = 20.sp
                    )
                    Text(
                        text = stringResource(R.string.nutrition),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        fontSize = 26.sp
                    )
                    Text(
                        text = stringResource(R.string.calories, recipeViewModel.calories),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        fontSize = 20.sp
                    )
                    Text(
                        text = stringResource(R.string.protein, recipeViewModel.protein),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        fontSize = 20.sp
                    )
                    Text(
                        text = stringResource(R.string.carbohydrates, recipeViewModel.carbohydrates),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        fontSize = 20.sp
                    )
                    Text(
                        text = stringResource(R.string.fat, recipeViewModel.fat),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        fontSize = 20.sp
                    )
                    Text(
                        text = stringResource(R.string.ingredients),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        fontSize = 26.sp
                    )
                    for (i in 0 until recipeViewModel.ingredients.length()){
                        Text(
                            text = recipeViewModel.ingredients[i].toString(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            fontSize = 20.sp
                        )
                    }
                    Text(
                        text = stringResource(R.string.instructions),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        fontSize = 26.sp
                    )
                    Text(
                        text = recipe.recipe_instructions,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        fontSize = 20.sp
                    )
                }
            }
        } else {
            item {
                Column {
                    Text(
                        text = stringResource(R.string.ChatGPT_error),
                        color = Color.Black,
                        fontSize = 28.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    Button(onClick = {navController.navigate(Screen.Home.route)}) {
                        Text(text = "Back")
                    }
                }
            }
        }
    }
}


