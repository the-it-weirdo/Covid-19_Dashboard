package dev.kingominho.covid_19dashboard.ui.about

import androidx.lifecycle.ViewModel

const val kingoMinho = "KingoMinho"

class AboutViewModel : ViewModel() {

    private val _data = initData()
    val data: List<Any>
        get() = _data


    private fun initData():List<Any> {
        val list: MutableList<Any> = ArrayList()

        list.add(AboutHeading("Project repository"))
        list.add(
            AboutItem(
                "Covid-19 Dashboard", "By $kingoMinho",
                "https://github.com/the-it-weirdo/Covid-19_Dashboard"
            )
        )

        list.add(AboutHeading("Built with: "))
        list.add(
            AboutItem(
                "Kotlin", "First class and official programming language for Android development.",
                "https://kotlinlang.org/"
            )
        )
        list.add(
            AboutItem(
                "Coroutines", "For asynchronous stuffs and more..",
                "https://kotlinlang.org/docs/reference/coroutines-overview.html"
            )
        )
        list.add(
            AboutItem(
                "Android Architecture Components", "Collection of libraries that help you design robust, testable, and maintainable apps.",
                "https://developer.android.com/topic/libraries/architecture"
            )
        )
        list.add(
            AboutItem(
                "WorkManager", "The WorkManager API makes it easy to schedule deferrable, asynchronous tasks that are expected to run even if the app exits or device restarts.",
                "https://developer.android.com/topic/libraries/architecture/workmanager"
            )
        )
        list.add(
            AboutItem(
                "Material Components for Android", "Modular and customizable Material Design UI components for Android.",
                "https://github.com/material-components/material-components-android"
            )
        )
        list.add(
            AboutItem(
                "Retrofit", "A type-safe HTTP client for Android and Java.",
                "https://square.github.io/retrofit/"
            )
        )
        list.add(
            AboutItem(
                "Moshi", "A modern JSON library for Kotlin and Java.",
                "https://github.com/square/moshi"
            )
        )
        list.add(
            AboutItem(
                "Moshi Converter", "A Converter which uses Moshi for serialization to and from JSON.",
                "https://github.com/square/retrofit/tree/master/retrofit-converters/moshi"
            )
        )

        list.add(AboutHeading("Resources used: "))
        list.add(
            AboutItem(
                "All icons from  ", "Flaticon by Freepik",
                "https://www.flaticon.com/authors/freepik"
            )
        )

        list.add(AboutHeading("Special Thanks: "))
        list.add(
            AboutItem(
                "Coding in Flow",
                "A special thanks to Florian Walther for creating " +
                        "awesome tutorials on Android Development.",
                "https://www.youtube.com/channel/UC_Fh8kvtkVPkeihBs42jGcA"
            )
        )
        list.add(
            AboutItem(
                "Stack Overflow", "For solving countless doubts.",
                "https://stackoverflow.com/"
            )
        )
        list.add(
            AboutItem(
                "Android Kotlin Fundamentals", "For helping me learn important concepts in Android Development.",
                "https://codelabs.developers.google.com/android-kotlin-fundamentals/"
            )
        )
        list.add(
            AboutItem(
                "Very special thanks to my friend Shouvit",
                "for providing valuable feedback and suggestions and helping me test my app and bearing with my constant nagging.",
                ""
            )
        )

        return list
    }
}