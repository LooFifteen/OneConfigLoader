dependencies {
    shade(project(":oneconfig-wrapper-launchwrapper"))
}

tasks {
    jar {
        manifest.attributes += mapOf(
            "ModSide" to "CLIENT",
            "ForceLoadAsMod" to true,
            "TweakOrder" to "0",
            "TweakClass" to "cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker"
        )
    }
}