package lunacrew.luna.util.extensions

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope

val processLifecycleScope: CoroutineScope
    get() = ProcessLifecycleOwner.get().lifecycleScope
