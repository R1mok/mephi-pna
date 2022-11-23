package com.example.petschedule.composables

sealed class Screen(val route : String) {
    object MainScreen : Screen("main_screen")
    object LoginPage : Screen("login_page")
    object MyGroups : Screen("my_groups")
    object WrongCredentials : Screen("wrong_credentials")
    object RegPage : Screen("reg_page")
    object LoginBusy : Screen("login_busy")
    fun withArgs(vararg args: String) : String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
