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

    const val THREAD_DETAILS = "thread-details/{threadId}"
    fun showThreadDetails(threadId: String) = "thread-details/$threadId"

    const val EXTEND_SPARK = "extend_spark/{threadId}/{currentSparkId}"
    fun extendSpark(threadId: String, currentSparkId: String) = "extend_spark/$threadId/$currentSparkId"

    const val ADD_START_SPARK = "add-spark/{threadId}"
    fun addStartSpark(threadId: String) = "add-spark/$threadId"


}

