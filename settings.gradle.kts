pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.polyfrost.org/releases")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "OneConfigStages"

mapOf(
    "stage0" to ("wrapper" to mapOf(
        "common" to "common",
        "forge" to "launchwrapper"
    )),
    "stage1" to ("loader" to mapOf(
        "common" to "common",
        "forge" to "launchwrapper"
    )),
    "testMod" to ("testMod" to mapOf(
        "forge" to "launchwrapper"
    ))
).forEach { (parent, project) ->
    val name = "oneconfig-${project.first}"
    include(name)
    project(":$name").projectDir = file(parent)
    project.second.forEach { (folder, display) ->
        val path = "$name-$display"
        include(path)
        project(":$path").projectDir = file("$parent/$folder")
    }
}