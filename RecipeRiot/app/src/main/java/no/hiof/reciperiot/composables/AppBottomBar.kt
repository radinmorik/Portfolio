package no.hiof.reciperiot.composables

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import no.hiof.reciperiot.Screen

@Composable
fun AppBottomBar(navController: NavController, bottomNavigationScreens: List<Screen>)
{
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    var nav = false
    val user = Firebase.auth.currentUser
    if (user != null){
        nav = true
    }

    BottomAppBar {
        bottomNavigationScreens.forEach { screen ->
            NavigationBarItem(selected = currentDestination == screen.route
                , onClick = {
                    navController.navigate(screen.route)
                }, icon = {
                    Icon(imageVector = screen.icon, contentDescription = "Icon")
                }, label = {
                    Text(stringResource(id = screen.title), fontSize = 11.sp, maxLines = 1)
                },
                enabled = nav
            )
        }
    }
}