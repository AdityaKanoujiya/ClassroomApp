pluginManagement {
    repositories {
        google()
        mavenCentral()
        jcenter()

        gradlePluginPortal()
        maven( url ="https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ClassroomApp"
include(":app")
 