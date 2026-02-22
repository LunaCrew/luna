package lunacrew.luna.auth

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun SignInForm(modifier: Modifier, callback: () -> Unit) {
    ConstraintLayout(modifier) {
        val (loginBtn) = createRefs()

        Button(
            onClick = { callback() },
            modifier = Modifier.constrainAs(loginBtn) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        ) {
            Text("Sign in")
        }
    }
}
