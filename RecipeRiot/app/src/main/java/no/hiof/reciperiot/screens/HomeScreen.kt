package no.hiof.reciperiot.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import no.hiof.reciperiot.R
import no.hiof.reciperiot.Screen
import no.hiof.reciperiot.ViewModels.HomeViewModel
import no.hiof.reciperiot.composables.NumberInput
import no.hiof.reciperiot.composables.RecipeList
import no.hiof.reciperiot.data.RecipeRepository
import okhttp3.OkHttpClient

@Composable
fun HomeScreen(navController: NavController, snackbarHost : SnackbarHostState, client: OkHttpClient, modifier: Modifier = Modifier, homeViewModel: HomeViewModel = viewModel()) {
    homeViewModel.updateIngredientsList()

    // Til snackbar
    val scope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Column(modifier = modifier.padding(horizontal = 50.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)){
            Text(stringResource(R.string.home_options), fontSize = 20.sp)
            NumberInput(text = stringResource(R.string.home_options_time), state = homeViewModel.time)
            Text(stringResource(R.string.home_ingredients), fontSize = 20.sp)
            LazyVerticalGrid(columns = GridCells.Adaptive(90.dp),
                content = {
                    items(homeViewModel.ingredients.size) {index ->
                        Text(homeViewModel.ingredients[index])
                    }
                }
            )
        }
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            val generateString = stringResource(id = R.string.snackbar_generate)
            val generatedString = stringResource(id = R.string.snackbar_generated)
            val errorString = stringResource(id = R.string.snackbar_error)
            Button(onClick = {
                scope.launch {
                    homeViewModel.buttonEnabled.value = false
                    snackbarHost.showSnackbar(generateString)
                }
                /*ChatGPT*/
                scope.launch {
                    val newRecipes = homeViewModel.generateGPT(client, homeViewModel.ingredients, homeViewModel.time.value)
                    //homeViewModel.recipes.value = newRecipes
                    var gotError = false
                    if (newRecipes[0].id.contains("error")){
                        gotError = true
                    }
                    homeViewModel.addToDatabase(newRecipes[0])
                    homeViewModel.buttonEnabled.value = true
                    if (gotError){
                        snackbarHost.showSnackbar(errorString)
                    }
                    else{
                        snackbarHost.showSnackbar(generatedString)
                    }
                }
            },
                enabled = homeViewModel.buttonEnabled.value) {
                Text(stringResource(R.string.home_generate))
            }
        }
        RecipeList(
            recipes = homeViewModel.recipes,
            navController = navController,
            onFavouriteToggle = {},
            updateRecipeFavouriteStatus = {recipe, fav ->
                RecipeRepository().updateRecipeFavouriteStatus(recipe, fav)
                Log.e("FirestoreError", "Error fetching data: ${homeViewModel.recipes}")
            }
        )
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.History.route) },
                modifier = modifier
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                Text(stringResource(R.string.prev_generate), modifier = Modifier.padding(16.dp))
            }
        }
    }
}