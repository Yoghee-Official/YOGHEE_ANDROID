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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

rootProject.name = "Yoghee"
include(":app")
include(":core:common")
include(":core:data")
include(":core:domain")
include(":core:ui")
include(":core:navigation")
include(":feature:main")
include(":feature:search")
include(":feature:category")
include(":feature:profile")
include(":feature:detail")
include(":feature:login")
include(":feature:contentFeed")
 