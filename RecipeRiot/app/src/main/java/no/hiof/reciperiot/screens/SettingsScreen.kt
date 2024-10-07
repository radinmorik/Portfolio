package no.hiof.reciperiot.screens

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import no.hiof.reciperiot.R



@Composable
fun SettingsScreen(logout: () -> Unit, darkTheme: MutableState<Boolean> = mutableStateOf(false), modifier : Modifier = Modifier) {
    val context = LocalContext.current
    var otherSetting by remember { mutableStateOf(false) }

    var langExpanded by remember { mutableStateOf(false)}
    val langItems = listOf(stringResource(R.string.english), stringResource(R.string.norwegian))
    var langSelectedIndex by remember { mutableStateOf(0) }
    val langLocaleStrings = listOf("en", "nb")

    Column(modifier = modifier
        .padding(horizontal = 50.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)){
        SwitchSetting(bool = darkTheme.value,
            update = { newValue ->
                darkTheme.value = newValue
                     }, text = stringResource(R.string.dark_theme)
        )
        SwitchSetting(bool = otherSetting, update = {newValue -> otherSetting = newValue}, text = "Other setting")
        Row{
            Text(stringResource(id = R.string.language))
            Box(modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopStart)
                .clip(shape = RoundedCornerShape(15.dp))) {
                Text(
                    langItems[langSelectedIndex],
                    Modifier
                        .clickable(onClick = { langExpanded = true })
                        .background(Color.Gray)
                        .padding(10.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                )

                DropdownMenu(expanded = langExpanded,
                    onDismissRequest = { langExpanded = false }) {
                    langItems.forEachIndexed { index, s ->
                        DropdownMenuItem(text = { Text(s) }, onClick = {
                            langSelectedIndex = index
                            langExpanded = false
                            changeLocales(context = context, langLocaleStrings[langSelectedIndex])
                        })
                    }
                }
            }
        }

    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Center)
    {
        Row (Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center){
            Button(onClick = { logout() }) {
                Text(stringResource(R.string.log_out))
            }
        }

    }
}

fun changeLocales(context: Context, localeString: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java)
            .applicationLocales = LocaleList.forLanguageTags(localeString)
    }
    else {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(localeString))
    }
}

@Composable
fun SwitchSetting(bool: Boolean, update: (Boolean) -> Unit, text: String){
    Row {
        Switch(checked = bool, onCheckedChange = {
            val newValue = !bool
            update(newValue)
        })
        Text(text)
    }
}