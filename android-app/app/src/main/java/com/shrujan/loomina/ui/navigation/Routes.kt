package com.shrujan.loomina.ui.navigation

object Routes {
    const val SPLASH = "splash"
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val REGISTER = "register"

    // main / nested nav graph root for bottom navigation
    const val MAIN = "main"

    const val HOME = "home"
    const val EXPLORE = "explore"
    const val CREATE = "create"
    const val LIBRARY = "library"
    const val PROFILE = "profile"
    const val CREATE_THREAD = "create-thread"
    const val CREATE_STORY = "create-story"

    const val CREATE_SPARK = "create_spark/{threadId}"
    fun createSpark(threadId: String) = "create_spark/$threadId"

    const val THREAD_DETAILS = "thread-details/{threadId}"
    fun showThreadDetails(threadId: String) = "thread-details/$threadId"
}
