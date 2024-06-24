import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("gg.essential.loom") version "1.3.polyfrost.3" apply false
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
    id("dev.architectury.architectury-pack200") version "0.1.3"
}

allprojects {
    group = "cc.polyfrost"
    version = "1.0.0-beta15"
}

subprojects {
    if (!project.hasProperty("oneconfig.java")) return@subprojects
    val shade = setupJavaProject()

    val loader = (project.findProperty("oneconfig.loader") ?: return@subprojects) as String
    if (loader == "launchwrapper") setupLaunchwrapperProject(shade)
}

fun Project.setupJavaProject(): Configuration {
    apply(plugin = "java")
    apply(plugin = "idea")
    apply(plugin = "maven-publish")

    val shade: Configuration by configurations.creating {
        configurations.named(JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME).get().extendsFrom(this)
        configurations.named(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME).get().extendsFrom(this)
    }

    configure<JavaPluginExtension> {
        withSourcesJar()
        toolchain.languageVersion.set(JavaLanguageVersion.of(8))
    }

    tasks.withType(JavaCompile::class) {
        options.encoding = "UTF-8"
    }

    repositories {
        mavenCentral()
    }

    configure<PublishingExtension> {
        repositories {
            mavenLocal()
            maven {
                name = "releases"
                setUrl("https://repo.polyfrost.cc/releases")
                credentials(PasswordCredentials::class)
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
            maven {
                name = "snapshots"
                setUrl("https://repo.polyfrost.cc/snapshots")
                credentials(PasswordCredentials::class)
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
            maven {
                name = "private"
                setUrl("https://repo.polyfrost.cc/releases")
                credentials(PasswordCredentials::class)
                authentication {
                    create<BasicAuthentication>("private")
                }
            }
        }
    }

    return shade
}

fun Project.setupLaunchwrapperProject(shade: Configuration) {
    println("setting up launchwrapper project for ${project.name}")

    apply(plugin = "gg.essential.loom")
    apply(plugin = "com.github.johnrengelman.shadow")

    configure<LoomGradleExtensionAPI> {
        runConfigs {
            "client" {
                programArgs("--tweakClass", "cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker")
            }
        }

        forge {
            pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
        }
    }

    dependencies {
        "minecraft"("com.mojang:minecraft:1.8.9")
        "mappings"("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
        "forge"("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")
    }

    tasks {
        withType(Jar::class) {
            archiveBaseName.set(project.name)
        }
        val shadowJar by named<ShadowJar>("shadowJar") {
            archiveClassifier.set("dev")
            configurations = listOf(shade)
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            if (project.name.contains("loader")) {
                relocate("cc.polyfrost.oneconfig.loader.stage0", "cc.polyfrost.oneconfig.loader")
            }
        }
        named<RemapJarTask>("remapJar") {
            inputFile.set(shadowJar.archiveFile)
            archiveClassifier.set("")
        }
        named<Jar>("jar") {
            dependsOn(shadowJar)
            archiveClassifier.set("")
            enabled = false
        }
    }
}


