package lunacrew.luna.core

sealed class Screen(val route: String) {
    object Main: Screen("main")
    object TopBar: Screen("topbar")
    object NavDrawer: Screen("nav-drawer")

    object AltComms: Screen("alt-comms")
    object Account: Screen("account")
    object Medical: Screen("medical")
    object Notepad: Screen("notepad")
    object Pomodoro: Screen("pomodoro")
    object Settings: Screen("settings")
}
