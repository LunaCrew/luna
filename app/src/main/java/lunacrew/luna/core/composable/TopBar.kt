package lunacrew.luna.core.composable

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.launch
import lunacrew.luna.R
import lunacrew.luna.core.MainViewModel
import lunacrew.luna.core.Screen
import lunacrew.luna.util.composables.colorScheme
import lunacrew.luna.util.extensions.getActivity
import lunacrew.luna.util.extensions.getDrawable
import lunacrew.luna.util.extensions.getString
import lunacrew.luna.util.extensions.popAllTo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    drawerState: DrawerState,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    CenterAlignedTopAppBar(
        title = {
            IconButton(onClick = {
                context.getActivity()?.let { activity ->
                    viewModel.navController.observe(activity) { controller ->
                        controller.popAllTo(Screen.Main.route)
                    }
                }
            }) {
                Icon(
                    painter = R.drawable.ic_logo.getDrawable(),
                    contentDescription = R.string.app_name.getString()
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                },
            ) {
                Icon(R.drawable.menu.getDrawable(), R.string.menu.getString())
            }
        },
        expandedHeight = 90.dp,
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        modifier = Modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorScheme().primary,
            scrolledContainerColor = colorScheme().primary,
            navigationIconContentColor = colorScheme().onPrimary,
            titleContentColor = colorScheme().onPrimary,
            actionIconContentColor = colorScheme().onPrimary,
            subtitleContentColor = colorScheme().onPrimary
        )
    )
}
