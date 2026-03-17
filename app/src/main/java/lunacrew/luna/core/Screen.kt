package lunacrew.luna.core

sealed class Screen(val route: String) {
    object Main: Screen("main")
    object Account: Screen("account")
    object AltComms: Screen("alt-comms")
    object Backup: Screen("backup")
    object Login: Screen("login")
    object Medical: Screen("medical")
    object Notepad: Screen("notepad")
    object Pomodoro: Screen("pomodoro")
    object Menu: Screen("menu")
}
