pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Eka News"
include(":app")
include(":core")
include(":feature")
include(":feature:headline")
include(":feature:saved")
include(":feature:headlinedetails")
include(":feature:search")
include(":core:model")
include(":core:network")
include(":core:database")
include(":core:data")
include(":core:ui")
