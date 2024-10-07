package no.hiof.reciperiot

import android.annotation.SuppressLint
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compose.AppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import no.hiof.reciperiot.composables.AppBottomBar
import no.hiof.reciperiot.composables.AppTopBar
import no.hiof.reciperiot.impl.NotificationService
import no.hiof.reciperiot.screens.AuthenticationScreen
import no.hiof.reciperiot.screens.FavouriteMeals
import no.hiof.reciperiot.screens.HistoryScreen
import no.hiof.reciperiot.screens.HomeScreen
import no.hiof.reciperiot.screens.IngredientsScreen
import no.hiof.reciperiot.screens.RecipePage1
import no.hiof.reciperiot.screens.SettingsScreen
import no.hiof.reciperiot.screens.ShoppingListScreen
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {

        val darkTheme = mutableStateOf(false)
        super.onCreate(savedInstanceState)
        val service = NotificationService(applicationContext)
        setContent {
            AppTheme(useDarkTheme = darkTheme.value) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp(service, client, db, darkTheme)
                }
            }
        }
    }
}
sealed class Screen(val route: String, val title: Int, val icon: ImageVector){
    object Login : Screen("login", R.string.nav_login, Icons.Default.Home)
    object Home : Screen("home", R.string.nav_home, Icons.Default.Home)
    object Ingredients : Screen("ingredients", R.string.nav_ingredients, Icons.Default.Add)
    object Favourites : Screen("favourites", R.string.nav_favourites, Icons.Default.Favorite)
    object Shopping : Screen("shopping", R.string.nav_shopping, Icons.Default.ShoppingCart)
    object Settings : Screen("settings", R.string.nav_settings, Icons.Default.Settings)
    object RecipePage : Screen("recipepage", R.string.nav_recipepage, Icons.Default.ArrowForward)
    object History : Screen("history", R.string.history, Icons.Default.List)

}
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainApp(notificationService: NotificationService, client: OkHttpClient,db: FirebaseFirestore, darkTheme: MutableState<Boolean>, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val bottomNavigationScreens = listOf(
        Screen.Home,
        Screen.Ingredients,
        Screen.Favourites,
        Screen.Shopping,
        Screen.Settings
    )

    val snackBarHostState = remember { SnackbarHostState() }

    val configuration = LocalConfiguration.current
    var topPadding = 100.dp

    Scaffold(
        topBar = {
            if (configuration.orientation != ORIENTATION_LANDSCAPE){
                AppTopBar(navController, modifier)
                topPadding = 100.dp
            }
            else{
                topPadding = 0.dp
            }
         },
        bottomBar = {
            AppBottomBar(
                navController = navController,
                bottomNavigationScreens = bottomNavigationScreens
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState)
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = Screen.Login.route, modifier = Modifier.padding(top = topPadding).padding(bottom = 80.dp)) {
            composable(Screen.Login.route) {
                AuthenticationScreen(
                    onSignInClick = { email, password ->
                        navController.navigate(Screen.Home.route)
                    },
                    showNotification = { user ->
                        notificationService.showNotification(user)
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(navController, snackBarHostState, client, modifier)
            }
            composable(Screen.Ingredients.route) {
                IngredientsScreen(navController, modifier, snackBarHostState)
            }
            composable(Screen.Favourites.route) {
                FavouriteMeals(navController)
            }
            composable(Screen.Shopping.route) {
                ShoppingListScreen(db = db)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(logout = {navController.navigate("login")
                    Firebase.auth.signOut()
                },
                    darkTheme)
            }
            composable("${Screen.RecipePage.route}/{recipeid}",
                arguments = listOf(navArgument("recipeid"){ type = NavType.StringType})
            ) { backStackEntry ->
                RecipePage1(navController, backStackEntry.arguments!!.getString("recipeid", "heipa"))
            }
            composable(Screen.History.route) {
                HistoryScreen(navController, db)
            }
        }
    }
}