package no.hiof.reciperiot.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.hiof.reciperiot.R
import no.hiof.reciperiot.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navController: NavController,
              modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier.padding(16.dp),
        title = { Text(text = "") },
        navigationIcon = {
            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painter = painterResource(id = R.drawable.reciperiot),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Home.route)
                    }
                )
            }
        }
    )
}