package lunacrew.luna.core.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.launch
import lunacrew.luna.R
import lunacrew.luna.core.MainViewModel
import lunacrew.luna.core.Screen
import lunacrew.luna.util.extensions.getActivity
import lunacrew.luna.util.extensions.getDrawable
import lunacrew.luna.util.extensions.getString
import lunacrew.luna.util.extensions.popAllTo
import lunacrew.luna.util.models.NavDrawerItem

@Composable
fun NavDrawer(
    drawerState: DrawerState,
    viewModel: MainViewModel = hiltViewModel(),
    content: @Composable (() -> Unit)
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    val items = listOf(
        NavDrawerItem(Screen.Main, R.drawable.home_filled, R.string.home),
        NavDrawerItem(Screen.Account, R.drawable.account_circle_filled, R.string.account),
        NavDrawerItem(Screen.Settings, R.drawable.settings_filled, R.string.settings),
        NavDrawerItem(Screen.AltComms, R.drawable.text_to_speech, R.string.alt_comms),
        NavDrawerItem(Screen.Medical, R.drawable.medical_information, R.string.medical_info),
        NavDrawerItem(Screen.Notepad, R.drawable.docs, R.string.notes),
        NavDrawerItem(Screen.Pomodoro, R.drawable.timer, R.string.pomodoro),
    )
    val selectedItem = remember { mutableStateOf(items[0]) }

    ModalNavigationDrawer (
        drawerState = drawerState,
        drawerContent = {
            DismissibleDrawerSheet(drawerState) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    Spacer(Modifier.height(12.dp))
                    items.forEach { item ->
                        NavigationDrawerItem(
                            icon = { Icon(item.icon.getDrawable(), item.title.getString()) },
                            label = { Text(item.title.getString()) },
                            selected = item == selectedItem.value,
                            onClick = {
                                scope.launch { drawerState.close() }
                                selectedItem.value = item
                                context.getActivity()?.let { activity ->
                                    viewModel.navController.observe(activity) { controller ->
                                        controller.popAllTo(item.screen.route)
                                    }
                                }
                            },
                            modifier = Modifier.padding(horizontal = 12.dp),
                        )
                    }
                }
            }
        },
        content = content
    )

    LaunchedEffect(drawerState.isClosed) {
        if (drawerState.isClosed) {
            // Keyboard focus should go back to button once drawer closes.
            focusRequester.requestFocus()
        }
    }
}
