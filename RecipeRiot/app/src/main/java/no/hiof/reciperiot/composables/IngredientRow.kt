package no.hiof.reciperiot.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.hiof.reciperiot.ViewModels.IngredientsViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IngredientRow(
    name: String,
    checkedState: MutableState<Boolean>,
    ingredientViewModel: IngredientsViewModel = viewModel(),
    onCheckedChange: (Boolean) -> Unit)
{
    val haptics = LocalHapticFeedback.current
    var expandedMenu by remember { mutableStateOf(false) }
    var menuRowId by rememberSaveable { mutableStateOf(name) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = name,
            modifier = Modifier
                .weight(1f)
                .combinedClickable(
                    onClick = {},
                    onLongClick = {
                        menuRowId = name
                        expandedMenu = true
                        haptics.performHapticFeedback(
                            HapticFeedbackType.LongPress
                        )
                    }
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Checkbox(
            checked = checkedState.value,
            onCheckedChange = { newValue ->
                onCheckedChange(newValue)
            },
            modifier = Modifier.size(24.dp)
        )
        DropdownMenu(expanded = expandedMenu, onDismissRequest = {expandedMenu = false}) {
            // When clicking delete, delete ingrident from firebase
            DropdownMenuItem(text = { Text(text = "Delete") }, onClick = {
                menuRowId.let { ingredientName ->
                    ingredientViewModel.deleteIngredient(ingredientName)
                }
            })
        }
    }
}