package lunacrew.luna.core

sealed class Screen(val route: String) {
    object Main: Screen("main")
    object AltComms: Screen("alt-comms")
    object Account: Screen("account")
    object Medical: Screen("medical")
    object Notepad: Screen("notepad")
    object Pomodoro: Screen("pomodoro")
    object Settings: Screen("settings")
}
